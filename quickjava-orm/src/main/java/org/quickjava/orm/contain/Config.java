package org.quickjava.orm.contain;

import java.sql.Connection;
import java.sql.SQLException;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Config
 * +-------------------------------------------------------------------
 * Date: 2023-6-1 9:52
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class Config {

    // 连接方式
    public DBSubject subject = DBSubject.CONFIG;

    // 数据库类型
    public DBType type = null;

    public String url = null;		//数据库地址

    public String database = null;		//数据库

    public String username = null;		//数据库用户名

    public String password = null;		//数据库密码

    // jdbc驱动
    public String driver = "com.mysql.jdbc.Driver";

    public Integer connectionFormType = 0;  // 0=自行连接；1=quickjava；2=spring

    public boolean autoCommit = true;

    public enum DBType {
        MYSQL,
        ORACLE,
        SQL_SERVER,
        UNKNOWN,
    }

    public enum DBSubject {
        CONFIG,
        QUICKJAVA,
        SPRING,
    }

    public Config(DBSubject subject, String url, String username, String password) {
        this.subject = subject;
        this.url = url;
        this.username = username;
        this.password = password;
        parseTypeFromUrl(url);
    }

    public Config(DBSubject subject) {
        this.subject = subject;
    }

    private void parseTypeFromUrl(String url)
    {
        if (url.toLowerCase().contains("mysql://")) {
            this.type = DBType.MYSQL;
        } else if (url.toLowerCase().contains("oracle://")) {
            this.type = DBType.ORACLE;
        }
    }

    private void parseTypeFromConnection(Connection connection) {
        try {
            String driverName = connection.getMetaData().getDriverName().toUpperCase();
            if (driverName.contains("MYSQL")) {
                this.type = DBType.MYSQL;
            } else if (driverName.contains("SQL SERVER")) {
                this.type = DBType.SQL_SERVER;
            } else if (driverName.contains("ORACLE")) {
                this.type = DBType.ORACLE;
            } else {
                this.type = DBType.UNKNOWN;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
