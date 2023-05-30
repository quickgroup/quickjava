package org.quickjava.orm.drive;

import org.quickjava.orm.utils.QueryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class QuickConnection {

    public DBType type = DBType.MYSQL;

    public String url = null;		//数据库地址

    public String database = null;		//数据库

    public String username = null;		//数据库用户名

    public String password = null;		//数据库密码

    public String driver = "com.mysql.jdbc.Driver";		//mysql驱动，测试写死

    public Connection connection = null;

    public Integer connectionFormType = 0;  // 0=自行连接；1=quickjava；2=spring

    public boolean autoCommit = true;

    public QuickConnection(DBType type, String url, String database, String username, String password, String driver) {
        this.type = type;
        this.url = url;
        this.database = database;
        this.username = username;
        this.password = password;
        this.driver = driver;
        init();
    }

    public QuickConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        init();
    }

    public QuickConnection(String url) {
        if (url.toLowerCase().contains("mysql://")) {
            this.type = DBType.MYSQL;
        } else if (url.toLowerCase().contains("oracle://")) {
            this.type = DBType.ORACLE;
        }
        this.url = url;
        init();
    }

    public QuickConnection(Connection connection, Integer connectionFormType) {
        this.connection = connection;
        this.connectionFormType = connectionFormType;
        init();
    }

    public void init() {
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

    public enum DBType {
        MYSQL,
        ORACLE,
        SQL_SERVER,
        UNKNOWN,
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
        connection.setAutoCommit(autoCommit);
    }

    public void startTrans() throws SQLException {
        setAutoCommit(false);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }
}
