package org.quickjava.common.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.util.Arrays;
import java.util.Date;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ReflectUtil
 * +-------------------------------------------------------------------
 * Date: 2023-5-29 17:30
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class ReflectUtil {

    private static final SimpleCache<Class<?>, Field[]> FIELDS_CACHE = new SimpleCache<>();
    private static final SimpleCache<Class<?>, Method[]> METHODS_CACHE = new SimpleCache<>();

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
            // getter方法获取
            String getterName = "get" + ComUtil.firstUpper(fieldName);
            Method method = getMethod(clazz, getterName);
            if (method == null || method.getParameterTypes().length > 0) {
                if (clazz.getSuperclass() != null) {
                    return getFieldValue(clazz.getSuperclass(), o, fieldName);
                }
            } else {
                method.setAccessible(true);
                return method.invoke(o);
            }

            // 直接属性获取值
            Field field = getField(clazz, fieldName);
            if (field == null) {
                if (clazz.getSuperclass() != null) {
                    return getFieldValue(clazz.getSuperclass(), o, fieldName);
                }
            } else {
                field.setAccessible(true);
                return field.get(o);
            }

        } catch (IllegalAccessException ignored) {
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
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

    public static Field[] getFields(Object o) {
        Class<?> clazz = o instanceof Class<?> ? (Class<?>) o : o.getClass();
        synchronized (ReflectUtil.FIELDS_CACHE) {
            if (!FIELDS_CACHE.contains(clazz)) {
                // TODO::只能当前类的字段
                FIELDS_CACHE.put(clazz, clazz.getDeclaredFields());
            }
        }
        return FIELDS_CACHE.get(clazz);
    }

    public static Field getField(Object o, String name) {
        return getField(o, name, false);
    }

    public static Field getField(Object o, String name, boolean parent) {
        for (Field field : getFields(o)) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        Class<?> currClazz = o instanceof Class ? (Class<?>) o : o.getClass();
        if (parent && currClazz.getSuperclass() != null) {
            return getField(currClazz.getSuperclass(), name, true);
        }
        return null;
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

    private static void setFieldValue(Class<?> clazz, Object o, String fieldName, Object value) {
        try {
            // 走setter设置
            String setterName = "set" + ComUtil.firstUpper(fieldName);
            Method method = getMethod(o, setterName, value);
            if (method != null) {       // java方法是可以被继承的
                method.invoke(o, value);
                return;
            }
            // 直接设置属性
            Field field = getField(clazz, fieldName, true);
            if (field != null) {
                field.setAccessible(true);
                field.set(o, valueConv(field.getType(), value));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValueDirect(Object o, String fieldName, Object value) {
        setFieldValueDirect(o.getClass(), o, fieldName, value);
    }

    public static void setFieldValueDirect(Class<?> clazz, Object o, String fieldName, Object value) {
        try {
            Field field = getField(clazz, fieldName);
            if (field != null) {
                value = valueConv(field.getType(), value);
                field.setAccessible(true);
                field.set(o, value);
            } else {
                if (clazz.getSuperclass() != null) {
                    setFieldValueDirect(clazz.getSuperclass(), o, fieldName, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValue(Object o, Field field, Object value) {
        try {
            // setter
            String setterName = "set" + ComUtil.firstUpper(field.getName());
            Method method = getMethod(o, setterName, value);
            if (method != null) {
                method.setAccessible(true);
                method.invoke(o, value);
                return;
            }
            // 直接设置
            field.setAccessible(true);
            field.set(o, value);

        } catch (Exception e) {
            System.out.println("设置对象字段数据时异常 o=" + o + "\nfield=" + field + ", value=" + value + "\nmsg=" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Method[] getMethods(Object o) {
        Class<?> clazz = o instanceof Class<?> ? (Class<?>) o : o.getClass();
        synchronized (METHODS_CACHE) {
            if (!METHODS_CACHE.contains(clazz)) {
                // 注意：这里会获取到继承父类的方法
                METHODS_CACHE.put(clazz, clazz.getDeclaredMethods());
            }
        }
        return METHODS_CACHE.get(clazz);
    }

    public static Method getMethod(Object o, String name, Object... args) {
        // 参数对象类
        Class<?>[] argsClasses = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                return null;
            }
            argsClasses[i] = args[i] instanceof Class<?> ? (Class<?>) args[i] : args[i].getClass();
        }
        for (Method method : getMethods(o)) {
            if (method.getName().equals(name) && Arrays.deepEquals(method.getParameterTypes(), argsClasses)) {
                return method;
            }
        }
        return null;
    }

    // FIXME::该方法兼容性差
    public static <T> T invoke(Object obj, String methodName, Object... args)
    {
        try {
            Method method = getMethod(obj, methodName, args);
            if (method == null) {
                return null;
            }
            method.setAccessible(true);
            return (T) method.invoke(obj, args);

        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
            return null;
        }
    }

    // 数据转换
    public static Object valueConv(Class<?> retClazz, Object value)
    {
        try {
            return DbClassConv.valueConv(retClazz, value);
        } catch (Exception ignored) {
        }
        return value;
    }

}
