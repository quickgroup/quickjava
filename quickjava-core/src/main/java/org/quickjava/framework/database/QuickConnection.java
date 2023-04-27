package org.quickjava.framework.database;

import org.quickjava.common.QuickLog;
import org.quickjava.framework.Lang;
import org.quickjava.framework.exception.QuickException;

import java.io.PrintWriter;
import java.io.StringWriter;
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

    /**
     * 连接数据库
     * @return
     */
    public Connection connectStart()
    {
        try {
            Class.forName(driver);
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new QuickException(Lang.to("数据库连接失败：" + e), e);
            }
        } catch (ClassNotFoundException e) {
            throw new QuickException(Lang.to(e.toString()), e);
        }
        return connection;
    }

    /**
     * 关闭数据库链接
     * @return
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
}
