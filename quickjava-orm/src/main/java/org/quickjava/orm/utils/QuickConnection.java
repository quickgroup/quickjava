package org.quickjava.orm.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class QuickConnection {

    private Type type = Type.MYSQL;

    private String url = null;		//数据库地址

    private String database = null;		//数据库

    private String username = null;		//数据库用户名

    private String password = null;		//数据库密码

    private String driver = "com.mysql.jdbc.Driver";		//mysql驱动，测试写死

    public Connection connection = null;

    public Integer connectionFormType = 0;  // 0=自行连接；1=quickjava；2=spring

    public boolean autoCommit = true;

    public QuickConnection(Type type, String url, String database, String username, String password, String driver) {
        this.type = type;
        this.url = url;
        this.database = database;
        this.username = username;
        this.password = password;
        this.driver = driver;
    }

    public QuickConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public QuickConnection(String url) {
        if (url.toLowerCase().contains("mysql://")) {
            this.type = Type.MYSQL;
        } else if (url.toLowerCase().contains("oracle://")) {
            this.type = Type.ORACLE;
        }
        this.url = url;
    }

    public QuickConnection(Connection connection, Integer connectionFormType) {
        this.connection = connection;
        this.connectionFormType = connectionFormType;
    }

    /**
     * 连接数据库
     * @return 数据库连接
     */
    public Connection connectStart()
    {
        try {
            Class.forName(driver);
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new QueryException("数据库连接失败：" + url + "=>" + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            throw new QueryException(e.toString());
        }
        return connection;
    }

    /**
     * 关闭数据库链接
     */
    public void close()
    {
        if(connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public enum Type {
        MYSQL,
        ORACLE,
    }

    public void setAutoCommit(boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
            this.autoCommit = autoCommit;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void startTrans() {
        setAutoCommit(false);
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();        // druid连接池：事务完成后返回连接
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();        // druid连接池：事务完成后返回连接
        }
    }
}
