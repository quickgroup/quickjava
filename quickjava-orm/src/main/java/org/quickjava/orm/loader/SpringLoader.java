package org.quickjava.orm.loader;

import org.quickjava.orm.contain.DatabaseConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
public class SpringLoader implements InitializingBean {

    private static ApplicationContext applicationContext;

    public static SpringLoader instance;

    @Autowired
    private DataSource druidDataSource;

    private DatabaseConfig.DBType dbType;

    private DatabaseConfig dbConfig;

    public SpringLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.instance = this;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public DataSource getDataSource() {
        return druidDataSource;
    }

    public DatabaseConfig.DBType getConnectionType() {
        synchronized (SpringLoader.class) {
            if (dbType == null) {
                try {
                    Connection connection = getDataSource().getConnection();
                    dbType = DatabaseConfig.parseTypeFromConnection(connection);
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return dbType;
    }

    public DatabaseConfig getConfig() {
        if (dbConfig == null) {
            dbConfig = new DatabaseConfig(DatabaseConfig.DBSubject.SPRING, getConnectionType());
        }
        return dbConfig;
    }
}
