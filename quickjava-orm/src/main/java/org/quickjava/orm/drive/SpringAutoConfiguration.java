package org.quickjava.orm.drive;

import org.quickjava.orm.contain.Config;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: SpringAutoConfiguration
 * +-------------------------------------------------------------------
 * Date: 2023-4-28 15:39
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
public class SpringAutoConfiguration implements InitializingBean {

    private static ApplicationContext applicationContext;

    public static SpringAutoConfiguration instance;

    @Autowired
    private DataSource druidDataSource;

    private Config.DBType dbType;

    public SpringAutoConfiguration(ApplicationContext applicationContext) {
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

    public Config.DBType getConnectionType() {
        synchronized (SpringAutoConfiguration.class) {
            if (dbType == null) {
                try {
                    Connection connection = getDataSource().getConnection();
                    dbType = Config.parseTypeFromConnection(connection);
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return dbType;
    }

    public Config getConfig() {
        return new Config(Config.DBSubject.SPRING, getConnectionType());
    }
}
