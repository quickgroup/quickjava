package org.quickjava.orm.utils;

import org.quickjava.orm.contain.ModelFieldMeta;
import org.quickjava.orm.contain.TableColumn;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: SqlUtils
 * +-------------------------------------------------------------------
 * Date: 2023-2-21 15:01
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class SqlUtil extends ORMHelper {

    // 数据转义
    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char src = str.charAt(i);
            switch (src) {
                case '\'':
                    sb.append('\\').append("'");
                    break;
                case '\"':
                case '\\':
                    sb.append('\\');
                default:
                    sb.append(src);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 是全大写字符串
     *
     * @param str 名称
     * @return 是否全大写
     */
    public static boolean isUpperString(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * List转字符串
     *
     * @param sequence 连接字符
     * @param iterable 可迭代对象
     * @param <T>      对象
     * @return 拼接语句
     */
    public static <T> String collJoin(CharSequence sequence, Iterable<T> iterable)
    {
        StringBuilder sb = new StringBuilder();
        for (T t : iterable) {
            sb.append(t).append(sequence);
        }
        if (sb.length() > sequence.length()) {
            sb.deleteCharAt(sb.length() - sequence.length());
        }
        return sb.toString();
    }

    /**
     * map的键连接
     *
     * @param str      语句
     * @param data      字段集
     * @param callback 处理方法
     */
    public static void mapBracketsJoin(StringBuilder str, Map<String, Object> data, MapJoinCallback callback) {
        str.append("(");
        int fi = 0;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (fi++ > 0)
                str.append(",");
            str.append(callback.call(entry));
        }
        str.append(")");
    }

    public static void mapBracketsJoin(StringBuilder str, Map<String, Object> data) {
        mapBracketsJoin(str, data, mapJoinKeyCallback);
    }

    public static void mapJoin(StringBuilder str, String symbol, Map<String, Object> data, MapJoinCallback callback) {
        int fi = 0;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (fi++ > 0)
                str.append(symbol);
            str.append(callback.call(entry));
        }
    }

    public static void mapJoin(StringBuilder str, Map<String, Object> data, MapJoinCallback callback) {
        mapJoin(str, ",", data, callback);
    }

    public static <D> StringBuilder listJoin(List<D> items, String symbol, MapJoinDCallback<D> callback) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0)
                str.append(symbol);
            str.append(callback.call(items.get(i)));
        }
        return str;
    }

    public static MapJoinCallback mapJoinKeyCallback = Map.Entry::getKey;

    public static MapJoinCallback mapJoinValueCallback = Map.Entry::getValue;

    public interface MapJoinCallback {
        Object call(Map.Entry<String, Object> entry);
    }

    public interface MapJoinDCallback<D> {
        Object call(D entry);
    }

    // 数据字段转驼峰
    public static Map<String, Object> dataFieldConv(Map<String, Object> data, Class<?> clazz) {
        Map<String, Object> ret = new LinkedHashMap<>();
        Map<String, ModelFieldMeta> fieldMap = ModelUtil.getMeta(clazz).fieldMap();
        data.forEach((k, v) -> {
            if (fieldMap.containsKey(SqlUtil.toCamelCase(k))) {
                ret.put(SqlUtil.toCamelCase(k), v);
            }
        });
        return ret;
    }

    // 缓存原始表字段
    public static Map<String, List<TableColumn>> tableColumnCache = new LinkedHashMap<>();

    public static List<TableColumn> getTableColumns(String table) {
        return tableColumnCache.get(table);
    }

    public static void setTableColumns(String table, List<TableColumn> columns) {
        tableColumnCache.put(table, columns);
    }

    // 数组查询
    public static boolean inArray(Object[] arr, Object target) {
        for (Object o : arr) {
            if (o == target) {
                return true;
            } else if (o != null && o.equals(target)) {
                return true;
            } else if (target != null && target.equals(o)) {
                return true;
            }
        }
        return false;
    }

    public static String tableColumn(String table, String column) {
        return table + "." + column;
    }

    /**
     * 字段别名
     */
    public static String fieldAlias(String table, String fieldName) {
        String column = SqlUtil.toUnderlineCase(fieldName);
        String columnAlias = table + "__" + column;
        return table + "." + column + " AS " + columnAlias;
    }

}
