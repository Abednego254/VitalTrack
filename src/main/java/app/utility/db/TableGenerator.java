package app.utility.db;

import app.framework.DbColumn;
import app.framework.DbTable;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * [CONCEPT: Database Generation from Annotations]
 * This utility reads our Magical Sticky Notes (@DbTable and @DbColumn)
 * and actually creates the tables in the database automatically!
 */
public class TableGenerator {

    public static void generateTables(Connection conn, Set<Class<?>> entityClasses) {
        for (Class<?> clazz : entityClasses) {

            // Only look at classes that have a @DbTable sticky note!
            if (!clazz.isAnnotationPresent(DbTable.class)) continue;

            DbTable table = clazz.getAnnotation(DbTable.class);
            String tableName = table.name();

            List<String> columns = new ArrayList<>();
            String primaryKey = null;

            for (Field field : clazz.getDeclaredFields()) {

                // Look for fields with the @DbColumn sticky note
                if (!field.isAnnotationPresent(DbColumn.class)) continue;

                DbColumn col = field.getAnnotation(DbColumn.class);
                String columnDef = col.name() + " " + col.type();

                if (col.autoIncrement()) {
                    columnDef += " AUTO_INCREMENT";
                }

                columns.add(columnDef);

                if (col.primaryKey()) {
                    primaryKey = col.name();
                }
            }

            if (primaryKey != null) {
                columns.add("PRIMARY KEY (" + primaryKey + ")");
            }

            String sql = "CREATE TABLE IF NOT EXISTS " + tableName +
                    " (" + String.join(", ", columns) + ")";

            execute(conn, sql);
        }
    }

    private static void execute(Connection conn, String sql) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Hospital Architect Built Table: " + sql);
        } catch (Exception e) {
            System.out.println("Failed to build table (might already exist or error): " + e.getMessage());
        }
    }
}
