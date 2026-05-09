package app.utility;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * [CONCEPT: JNDI & Container-Managed Resources]
 * This class no longer manages DB credentials. 
 * Instead, it asks WildFly for a "DataSource" by its JNDI name.
 */
@ApplicationScoped
public class DataSourceHelper {

    @Resource(lookup = "java:jboss/datasources/VitalTrackDS")
    private DataSource dataSource;

    @Produces
    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("!!! KEY HOLDER ERROR: WildFly has not provided the DataSource 'java:jboss/datasources/VitalTrackDS'");
        }
        return dataSource.getConnection();
    }
}
