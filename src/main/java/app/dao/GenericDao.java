package app.dao;

import app.framework.DbColumn;
import app.framework.DbTable;
import app.utility.DataSourceHelper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GenericDao<T, ID> {

    private final Class<T> entityClass;
    private final String tableName;
    private final List<Field> columns = new ArrayList<>();
    private Field idField;

    private DataSourceHelper dataSourceHelper;

    public GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;

        if (!entityClass.isAnnotationPresent(DbTable.class)) {
            throw new RuntimeException("Missing @DbTable on " + entityClass.getName());
        }

        this.tableName = entityClass.getAnnotation(DbTable.class).name();

        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(DbColumn.class)) {
                field.setAccessible(true);
                columns.add(field);

                if (field.getAnnotation(DbColumn.class).primaryKey()) {
                    idField = field;
                }
            }
        }

        if (idField == null) {
            throw new RuntimeException("No primary key defined in " + entityClass.getName());
        }
    }

    public void save(T entity) {
        try (Connection conn = dataSourceHelper.getConnection()) {

            List<String> colNames = new ArrayList<>();
            List<String> placeholders = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            // 1. Read values from the fields
            for (Field field : columns) {
                DbColumn col = field.getAnnotation(DbColumn.class);

                if (col.autoIncrement()) continue;

                colNames.add(col.name());
                placeholders.add("?");
                values.add(field.get(entity));
            }

            // 2. Build the SQL Laser Command
            String sql = "INSERT INTO " + tableName +
                    " (" + String.join(",", colNames) + ") VALUES (" +
                    String.join(",", placeholders) + ")";

            PreparedStatement ps = conn.prepareStatement(sql);

            // 3. Put the values into the command
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }

            // 4. Execute the command!
            ps.executeUpdate();
            System.out.println("********** DAO: Saved to " + tableName + " **********");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<T> findAll() {
        List<T> list = new ArrayList<>();

        try (Connection conn = dataSourceHelper.getConnection()) {

            String sql = "SELECT * FROM " + tableName;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public T findById(ID id) {
        try (Connection conn = dataSourceHelper.getConnection()) {
            String idColName = idField.getAnnotation(DbColumn.class).name();
            String sql = "SELECT * FROM " + tableName + " WHERE " + idColName + " = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(ID id) {
        try (Connection conn = dataSourceHelper.getConnection()) {
            String idColName = idField.getAnnotation(DbColumn.class).name();
            String sql = "DELETE FROM " + tableName + " WHERE " + idColName + " = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, id);
            ps.executeUpdate();
            System.out.println("********** DAO: Deleted from " + tableName + " ID=" + id + " **********");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(T entity) {
        try (Connection conn = dataSourceHelper.getConnection()) {
            List<String> setClauses = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            String idColName = idField.getAnnotation(DbColumn.class).name();
            Object idValue = idField.get(entity);

            for (Field field : columns) {
                DbColumn col = field.getAnnotation(DbColumn.class);
                if (col.primaryKey()) continue;

                setClauses.add(col.name() + " = ?");
                values.add(field.get(entity));
            }

            String sql = "UPDATE " + tableName + " SET " + String.join(", ", setClauses) +
                         " WHERE " + idColName + " = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            ps.setObject(values.size() + 1, idValue);

            ps.executeUpdate();
            System.out.println("********** DAO: Updated " + tableName + " ID=" + idValue + " **********");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper: Creates a fresh object and fills it with data from the database
    private T mapResultSet(ResultSet rs) throws Exception {
        T instance = entityClass.getDeclaredConstructor().newInstance();

        for (Field field : columns) {
            DbColumn col = field.getAnnotation(DbColumn.class);
            Object value = rs.getObject(col.name());
            field.set(instance, value);
        }

        return instance;
    }

    public DataSourceHelper getDs() {
        return dataSourceHelper;
    }

    public void setDs(DataSourceHelper ds) {
        this.dataSourceHelper = ds;
    }
}
