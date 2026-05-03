package app.utility;

import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.annotation.PreDestroy;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * [CONCEPT: DATASOURCE + @Produces + @PreDestroy]
 * This is our "Magic Key Holder".
 * It knows how to open the door to the MySQL Vault.
 */
@ApplicationScoped
public class DataSourceHelper {

    private static final String DB_NAME = "vitaltrack_db";
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_URL = SERVER_URL + DB_NAME + "?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASS = "Ciumbe@254";

    private MysqlDataSource dataSource;

    /**
     * [CONCEPT: @Produces]
     * By adding @Produces here, we tell the CDI container:
     * "Whenever ANY class needs a DataSource, call THIS method
     *  to get the right one. Don't guess — I'll build it for you!"
     *
     * Now any bean can do: @Inject DataSource ds;
     * and the container will call this method automatically!
     */
    @Produces
    public DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new MysqlDataSource();
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

    /**
     * [CONCEPT: @PreDestroy]
     * This method runs automatically just before the container
     * destroys this bean (i.e., when WildFly is shutting down).
     *
     * Like a staff member returning their keys on their last day!
     */
    @PreDestroy
    public void cleanup() {
        System.out.println("********** KEY HOLDER: Hospital is closing. Returning vault keys! **********");
        // In a real connection pool, we would close the pool here.
        dataSource = null;
    }
}
