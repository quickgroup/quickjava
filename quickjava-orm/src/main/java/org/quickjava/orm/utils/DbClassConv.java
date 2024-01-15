package org.quickjava.orm.utils;

import cn.hutool.core.convert.Convert;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: DbClassConv
 * +-------------------------------------------------------------------
 * Date: 2023-5-30 18:04
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class DbClassConv {

    public static Object valueConv(Class<?> clazz, Object value) {
        try {
            if (clazz == String.class) {
                return convertResultSetToString(value);
            } else if (clazz == int.class || clazz == Integer.class) {
                return convertObjectToInt(value);
            } else if (clazz == long.class || clazz == Long.class) {
                return convertObjectToLong(value);
            } else if (clazz == double.class || clazz == Double.class) {
                return convertObjectToDouble(value);
            } else if (clazz.isEnum()) {
                return convertObjectToEnum(clazz, value);
            }
        } catch (Exception ignore) {
        }

        return Convert.convert(clazz, value);
    }

    public static String convertResultSetToString(Object value) throws SQLException, IOException {
        if (value instanceof Date) {
            value = (new SimpleDateFormat("yyyy-MM-dd")).format(value);
        } else if (value instanceof Time) {
            value = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(value);
        } else if (value instanceof Timestamp) {
            value = (new SimpleDateFormat("HH:mm:ss")).format(value);
        } else if (value instanceof BigDecimal) {
            value = value.toString();
        } else if (value instanceof Blob) {
            Blob blob = (Blob) value;
            value = new String(blob.getBytes(1, (int) blob.length()));
        } else if (value instanceof Clob) {
            value = convertClobToString((Clob) value);
        }
        return Convert.convert(String.class, value);
    }

    private static String convertClobToString(Clob clob) throws SQLException, IOException {
        // 将 Clob 类型转换为 String 的逻辑
        StringBuilder sb = new StringBuilder();
        Reader reader = clob.getCharacterStream();
        try {
            char[] buffer = new char[4096];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, bytesRead);
            }
        } finally {
            reader.close();
        }
        return sb.toString();
    }

    public static Integer convertObjectToInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException ignore) {
        }
        return Convert.convert(Integer.class, value);
    }

    public static long convertObjectToLong(Object value) {
        if (value == null) {
            return 0L;
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static double convertObjectToDouble(Object value) {
        if (value == null) {
            return 0.0;
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static boolean convertObjectToBoolean(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean) {
            return (boolean) value;
        }

        String strValue = value.toString().toLowerCase();
        return strValue.equals("true") || strValue.equals("1");
    }

    public static Object convertObjectToEnum(Class clazz, Object value) {
        return Enum.valueOf(clazz, value.toString());
    }

}
