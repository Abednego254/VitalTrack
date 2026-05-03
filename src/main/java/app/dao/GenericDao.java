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

/**
 * [CONCEPT: Generic DAO Layer]
 * This is our "Storage Room Worker".
 * Instead of Servlets going to the database, they hand data to the EJB,
 * and the EJB hands it to this worker.
 *
 * This worker uses the "Magical Sticky Notes" (@DbTable and @DbColumn)
 * to figure out exactly how to save or fetch the data.
 */
public class GenericDao<T, ID> {

    private final Class<T> entityClass;
    private final String tableName;
    private final List<Field> columns = new ArrayList<>();
    private Field idField;

    private final DataSourceHelper dataSourceHelper;

    public GenericDao(Class<T> entityClass, DataSourceHelper dataSourceHelper) {
        this.entityClass = entityClass;
        this.dataSourceHelper = dataSourceHelper;

        // 1. Look for the @DbTable sticky note on the class
        if (!entityClass.isAnnotationPresent(DbTable.class)) {
            throw new RuntimeException("Missing @DbTable on " + entityClass.getName());
        }

        // 2. Read the table name from the sticky note
        this.tableName = entityClass.getAnnotation(DbTable.class).name();

        // 3. Look at all the fields and find the @DbColumn sticky notes
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(DbColumn.class)) {
                field.setAccessible(true);
                columns.add(field);

                // 4. Find the special ID field
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
}
