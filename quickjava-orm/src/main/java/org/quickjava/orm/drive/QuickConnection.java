package org.quickjava.orm.drive;

import org.quickjava.orm.contain.DatabaseConfig;
import org.quickjava.orm.utils.QueryException;
import org.quickjava.orm.utils.QuickORMException;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QuickConnection {

    private DatabaseConfig config;

    private Connection connection;

    public boolean autoCommit;

    public QuickConnection(DatabaseConfig config) {
        this.config = config;
        this.autoCommit = config.autoCommit;
    }

    /**
     * 连接数据库
     * @return 数据库连接
     */
    public QuickConnection connect()
    {
        // 根据配置连接 or QuickJava框架中使用
        if (config.subject == DatabaseConfig.DBSubject.CONFIG || config.subject == DatabaseConfig.DBSubject.QUICKJAVA) {
            try {
                Class.forName(config.driver);
                this.connection = DriverManager.getConnection(config.url, config.username, config.password);
            }  catch (ClassNotFoundException e) {
                throw new QueryException(e.toString());
            } catch (SQLException e) {
                throw new QueryException("数据库连接失败：" + config.url + "=>" + e.getMessage());
            }
        }

        // Spring 框架中使用
        else if (config.subject == DatabaseConfig.DBSubject.SPRING) {
            try {
                this.connection = SpringAutoConfiguration.instance.getDataSource().getConnection();
            } catch (SQLException e) {
                throw new QueryException("获取连接失败");
            }
        }

        return this;
    }

    public void setAutoCommit(boolean autoCommit) {
        try {
            this.autoCommit = autoCommit;
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new QuickORMException(e.getMessage());
        }
    }

    public void setTransactionIsolation(int level) {
        try {
            connection.setTransactionIsolation(level);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new QuickORMException(e.getMessage());
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭数据库链接
     */
    public void close()
    {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 数据插入
     * @param sql 语句
     * @return map
     * @throws SQLException e
     */
    public Map<String, Object> insert(String sql)
            throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

        ResultSet generatedKeys = statement.getGeneratedKeys();
        List<String> fieldNameArr = prepareField(generatedKeys);
        if (fieldNameArr.isEmpty()) {
            return null;
        }
        Map<String, Object> map = new LinkedHashMap<>();
        generatedKeys.next();
        for (int fi = 1; fi <= generatedKeys.getRow(); fi++) {
            map.put(fieldNameArr.get(fi - 1), generatedKeys.getObject(fi));
        }
        return map;
    }

    public Integer update(String sql)
            throws SQLException
    {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public Integer delete(String sql)
            throws SQLException
    {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public List<Map<String, Object>> select(String sql)
            throws SQLException
    {
        List<Map<String, Object>> rows = new LinkedList<>();
        ResultSet resultSet = null;

        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            List<String> fieldNameArr = prepareField(resultSet);
            // 数据组装
            while (resultSet.next()) {
                Map<String, Object> item = new LinkedHashMap<>();
                for (int fi = 0; fi < fieldNameArr.size(); fi++) {
                    item.put(fieldNameArr.get(fi), resultSet.getObject(fi + 1));
                }
                rows.add(item);
            }

        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }

        return rows;
    }

    private static List<String> prepareField(ResultSet resultSet) throws SQLException
    {
        ResultSetMetaData metaData = resultSet.getMetaData();
        List<String> fieldNameArr = new LinkedList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            fieldNameArr.add(metaData.getColumnLabel(i));
        }
        return fieldNameArr;
    }
}
