package app.utility;

import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * [CONCEPT: DATASOURCE]
 * This is our "Magic Key Holder". 
 * It knows exactly how to open the door to the MySQL Vault.
 */
@ApplicationScoped
public class DataSourceHelper {

    private static final String DB_NAME = "vitaltrack_db";
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_URL = SERVER_URL + DB_NAME + "?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASS = "Ciumbe@254";

    private MysqlDataSource dataSource;

    public DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new MysqlDataSource();
            // The magic flag 'createDatabaseIfNotExist=true' helps us!
            dataSource.setURL(DB_URL);
            dataSource.setUser(USER);
            dataSource.setPassword(PASS);
            
            System.out.println("********** KEY HOLDER: Vault " + DB_NAME + " is ready! **********");
        }
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}
