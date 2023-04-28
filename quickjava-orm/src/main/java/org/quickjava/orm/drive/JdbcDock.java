/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.drive;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/***
 * 调用JDBC方法
 */
public class JdbcDock {

    public static Map<String, Object> insert(Connection connection, String sql)
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

    public static Integer update(Connection connection, String sql)
            throws SQLException
    {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public static Integer delete(Connection connection, String sql)
            throws SQLException
    {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public static List<Map<String, Object>> select(Connection connection, String sql)
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

    public static List<String> prepareField(ResultSet resultSet) throws SQLException
    {
        ResultSetMetaData metaData = resultSet.getMetaData();
        List<String> fieldNameArr = new LinkedList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            fieldNameArr.add(metaData.getColumnLabel(i));
        }
        return fieldNameArr;
    }

}
