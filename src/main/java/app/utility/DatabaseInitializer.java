package app.utility;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * [CONCEPT: DATABASE INITIALIZATION]
 * This helper reads our "Building Plan" (schema.sql) and builds 
 * the drawers (tables) in our MySQL vault.
 */
@ApplicationScoped
public class DatabaseInitializer {

    @Inject
    private DataSourceHelper dataSourceHelper;

    public void initialize() {
        System.out.println("********** BUILDER: Starting to build the drawers... **********");
        
        try (Connection conn = dataSourceHelper.getConnection();
             InputStream is = getClass().getClassLoader().getResourceAsStream("schema.sql")) {

            if (is == null) {
                System.out.println("ERROR: Could not find schema.sql!");
                return;
            }

            // Read the SQL script
            String sql = new BufferedReader(new InputStreamReader(is))
                    .lines().collect(Collectors.joining("\n"));

            // Split by semicolon to run each command separately
            String[] commands = sql.split(";");

            try (Statement stmt = conn.createStatement()) {
                for (String command : commands) {
                    if (!command.trim().isEmpty()) {
                        stmt.execute(command);
                    }
                }
            }

            System.out.println("********** BUILDER: All drawers are ready! **********");

        } catch (Exception e) {
            System.err.println("ERROR during database initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
