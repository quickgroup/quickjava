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
public class DatabaseMeta {

    // 连接处理框架
    public String subject = null;

    // 数据库类型
    public DBType type = DBType.MYSQL;

    // 连接语句
    public String url = null;

    // 数据库名称
    public String database = null;

    // jdbc驱动
    public String driver = "com.mysql.jdbc.Driver";

    public boolean autoCommit = true;

    // 字段转大写
    public boolean camelCase = true;

    // 属性转下划线列名
    public boolean underline = true;

    public enum DBType {
        MYSQL,
        ORACLE,
        SQL_SERVER,
        DEFAULT,
    }

    public DatabaseMeta(String subject, String url) {
        this.subject = subject;
        parseDBTypeFromUrl(url);
    }

    public DatabaseMeta(String subject, DBType dbType) {
        this.subject = subject;
        this.type = dbType;
    }

    private void parseDBTypeFromUrl(String url)
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setType(DBType type) {
        this.type = type;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public boolean isCamelCase() {
        return camelCase;
    }

    public void setCamelCase(boolean camelCase) {
        this.camelCase = camelCase;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    @Override
    public String toString() {
        return "DatabaseMeta{" +
                "subject='" + subject + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", database='" + database + '\'' +
                ", driver='" + driver + '\'' +
                ", autoCommit=" + autoCommit +
                ", camelCase=" + camelCase +
                ", underline=" + underline +
                '}';
    }
}
