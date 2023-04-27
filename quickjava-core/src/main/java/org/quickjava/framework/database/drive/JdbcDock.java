/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework.database.drive;

import org.quickjava.common.QuickLog;

import java.sql.*;
import java.util.*;

/***
 * 调用JDBC方法
 */
public class JdbcDock {

    public static Map insert(Connection connection, String sql)
            throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

        ResultSet generatedKeys = statement.getGeneratedKeys();
        List<String> fieldNameArr = parseField(generatedKeys);
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

    public static List<Map> select(Connection connection, String sql)
            throws SQLException
    {
        ResultSet resultSet = null;
        List<Map> rows = new LinkedList<>();

        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();
//            QuickLog.debug("结果数：" + rowCount);

            List<String> fieldNameArr = parseField(resultSet);

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

    public static List<String> parseField(ResultSet resultSet)
            throws SQLException
    {
        ResultSetMetaData metaData = resultSet.getMetaData();
        List<String> fieldNameArr = new LinkedList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            fieldNameArr.add(metaData.getColumnName(i));
        }
        return fieldNameArr;
    }

}
