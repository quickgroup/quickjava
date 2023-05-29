package org.quickjava.orm.utils;

import org.quickjava.orm.contain.ModelField;
import org.quickjava.orm.contain.TableColumn;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    /**
     * 获取类上的泛型
     *
     * @param clazz 目标类
     * @param index 泛型位置
     * @return 泛型实际类
     */
    public static Class<?> getGenericsType(Class<?> clazz, int index) {
        return GenericsUtils.getSuperClassGenericsType(clazz, index);
    }

    /**
     * 直接读取属性值，不走getter
     *
     * @param o         对象
     * @param fieldName 属性名
     * @return 属性值
     */
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
        } catch (IllegalAccessException ignored) {
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

    // FIXME::该方法兼容性差
    public static <T> T invoke(Object obj, String methodName, Object... args)
    {
        try {
            Class<?>[] argsClasses = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argsClasses[i] = args[i] instanceof Class<?> ? (Class<?>) args[i] : args[i].getClass();
            }
            Method method = obj.getClass().getDeclaredMethod(methodName, argsClasses);
            method.setAccessible(true);
            return (T) method.invoke(obj, args);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 直接设置属性值，不走setter
     *
     * @param o     对象
     * @param field 属性名
     * @param value 属性值
     */
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
            // setter
            String setterName = field.getName();
            setterName = "set" + setterName.substring(0, 1).toUpperCase() + setterName.substring(1);
            Method method = o.getClass().getDeclaredMethod(setterName, value.getClass());
            method.setAccessible(true);
            method.invoke(o, value);
//            ReflectUtil.setFieldValue(o, field, value);

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
     * 反引号包围
     *
     * @param str 字段名称
     * @return 结果
     */
    public static String backQuote(String str) {
        return "`" + str + "`";
    }

    /**
     * List转字符串
     *
     * @param sequence 连接字符
     * @param iterable 可迭代对象
     * @param <T>      对象
     * @return 拼接语句
     */
    public static <T> String collJoin(CharSequence sequence, Iterable<T> iterable) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (T t : iterable) {
            sb.append(t).append(sequence);
            count++;
        }
        if (count > 1) {
            sb.deleteCharAt(sb.length() - sequence.length());
        }
        return sb.toString();
    }

    /**
     * map的键连接
     *
     * @param str      语句
     * @param map      字段集
     * @param callback 处理方法
     */
    public static void mapKeyJoin(StringBuilder str, Map<String, Object> map, MapJoinCallback callback) {
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
     *
     * @param str      构造语句
     * @param data     数据集
     * @param callback 数据处理方法
     */
    public static void mapValueJoin(StringBuilder str, Map<String, Object> data, MapJoinCallback callback) {
        str.append("(");
        int fi = 0;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
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
            if (fieldMap.containsKey(SqlUtil.toCamelCase(k))) {
                ret.put(SqlUtil.toCamelCase(k), v);
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

}
