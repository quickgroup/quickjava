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

    public boolean autoCommit = true;

    public boolean underscoreToCamelCase = true;

    public enum DBType {
        MYSQL,
        ORACLE,
        SQL_SERVER,
        DEFAULT,
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

    public Config(DBSubject subject, DBType dbType) {
        this.subject = subject;
        this.type = dbType;
    }

    private void parseTypeFromUrl(String url)
    {
        if (url.toLowerCase().contains("mysql://")) {
            this.type = DBType.MYSQL;
        } else if (url.toLowerCase().contains("oracle://")) {
            this.type = DBType.ORACLE;
        } else {
            this.type = DBType.DEFAULT;
        }
    }

    public static DBType parseTypeFromConnection(Connection connection) {
        try {
            String driverName = connection.getMetaData().getDriverName().toUpperCase();
            if (driverName.contains("MYSQL")) {
                return DBType.MYSQL;
            } else if (driverName.contains("SQL SERVER")) {
                return DBType.SQL_SERVER;
            } else if (driverName.contains("ORACLE")) {
                return DBType.ORACLE;
            } else {
                return DBType.DEFAULT;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DBType getType() {
        if (type != null) {
            return type;
        }
        throw new RuntimeException("未配置连接类型的配置");
    }

    @Override
    public String toString() {
        return "Config{" +
                "subject=" + subject +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", database='" + database + '\'' +
                ", username='" + username + '\'' +
                ", driver='" + driver + '\'' +
                ", autoCommit=" + autoCommit +
                ", underscoreToCamelCase=" + underscoreToCamelCase +
                '}';
    }
}
