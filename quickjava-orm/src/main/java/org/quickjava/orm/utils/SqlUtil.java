package org.quickjava.orm.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.quickjava.orm.contain.ModelField;
import org.quickjava.orm.contain.TableColumn;

import java.lang.reflect.Field;
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
public class SqlUtil {

    /**
     * 获取类上的泛型
     * */
    public static Class<?> getGenericsType(Class<?> clazz, int index) {
        return GenericsUtils.getSuperClassGenericsType(clazz, index);
    }

    /**
     * 直接读取属性值，不走getter
     * */
    public static Object getFieldValue(Object o, String fieldName) {
        return getFieldValue(o.getClass(), o, fieldName);
    }

    public static Object getFieldValue(Class<?> clazz, Object o, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(o);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {    // 向上找父类的属性
                return getFieldValue(clazz.getSuperclass(), o, fieldName);
            }
        } catch(IllegalAccessException ignored) {
        }
        return null;
    }

    public static Object getFieldValue(Object o, Field field) {
        try {
            field.setAccessible(true);
            return field.get(o);
        } catch (IllegalAccessException ignored) {
            return null;
        }
    }

    /**
     * 直接设置属性值，不走setter
     * */
    public static void setFieldValue(Object o, String field, Object value) {
        setFieldValue(o.getClass(), o, field, value);
    }

    private static void setFieldValue(Class<?> clazz, Object o, String field, Object value) {
        try {
            setFieldValue(o, clazz.getDeclaredField(field), value);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {    // 向上找父类的属性
                setFieldValue(clazz.getSuperclass(), o, field, value);
            }
//            e.printStackTrace();
        }
    }

    public static void setFieldValue(Object o, Field field, Object value) {
        try {
            field.setAccessible(true);
            ReflectUtil.setFieldValue(o, field, value);
        } catch (Exception e) {
            System.out.println("设置对象字段数据时异常 o=" + o + "\nfield=" + field + ", value=" + value + "\nmsg=" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 数据转义
    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char src = str.charAt(i);
            switch (src) {
                case '\'': sb.append('\\').append("'"); break;
                case '\"':
                case '\\': sb.append('\\');
                default: sb.append(src); break;
            }
        }
        return sb.toString();
    }

    public static boolean isEmpty(Object obj) {
        return ObjectUtil.isEmpty(obj);
    }

    /**
     * 是全大写字符串
     * */
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
     * 反引号包围
     * */
    public static String backQuote(String str) {
        return "`" + str + "`";
    }

    /**
     * 转为标准实体属性驼峰名称，如：userType，和 {@link SqlUtil#fieldLineName} 相反用法
     * */
    public static String fieldName(String field) {
        return StrUtil.toCamelCase(field);
    }

    /**
     * 转为下划线字段名称，如：user_type，和 {@link SqlUtil#fieldName} 相反用法
     * */
    public static String fieldLineName(String field) {
        return StrUtil.toUnderlineCase(field);
    }

    /**
     * List转字符串
     * */
    public static <T> String strJoin(CharSequence conjunction, Iterable<T> iterable) {
        return CollUtil.join(iterable, conjunction);
    }

    /**
     * map的键连接
     * */
    public static void mapKeyJoin(StringBuilder str, Map<String, Object> map, MapJoinCallback callback)
    {
        str.append("(");
        int fi = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (fi++ > 0)
                str.append(",");
            str.append(callback.call(entry));
        }
        str.append(")");
    }

    public static void mapKeyJoin(StringBuilder str, Map<String, Object> map) {
        mapKeyJoin(str, map, mapJoinKeyCallback);
    }

    /**
     * map的键连接
     * */
    public static void mapValueJoin(StringBuilder str, Map<String, Object> map, MapJoinCallback callback)
    {
        str.append("(");
        int fi = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (fi++ > 0)
                str.append(",");
            str.append(callback.call(entry));
        }
        str.append(")");
    }

    public static void mapValueJoin(StringBuilder str, Map<String, Object> map) {
        mapValueJoin(str, map, mapJoinValueCallback);
    }

    public static MapJoinCallback mapJoinKeyCallback = Map.Entry::getKey;

    public static MapJoinCallback mapJoinValueCallback = Map.Entry::getValue;

    public interface MapJoinCallback {
        Object call(Map.Entry<String, Object> entry);
    }

    // 数据字段转驼峰
    public static Map<String, Object> dataFieldConv(Map<String, Object> data, Class<?> clazz) {
        Map<String, Object> ret = new LinkedHashMap<>();
        Map<String, ModelField> fieldMap = ModelUtil.getMeta(clazz).fieldMap();
        data.forEach((k, v) -> {
            if (fieldMap.containsKey(SqlUtil.fieldName(k))) {
                ret.put(SqlUtil.fieldName(k), v);
            }
        });
        return ret;
    }

    // 表信息缓存
    public static Map<String, List<TableColumn>> tableColumnCache = new LinkedHashMap<>();

    public static List<TableColumn> getTableColumns(String table) {
        return tableColumnCache.get(table);
    }

    public static void setTableColumns(String table, List<TableColumn> columns) {
        tableColumnCache.put(table, columns);
    }

}
