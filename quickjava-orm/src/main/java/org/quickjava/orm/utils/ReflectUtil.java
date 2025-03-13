package org.quickjava.orm.utils;

import org.quickjava.common.utils.SimpleCache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

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
     * @param o object
     * @param fieldName fieldName
     * @return ret
     * @param <T> T
     */
    public static<T> T getFieldValue(Object o, String fieldName) {
        return getFieldValue(o.getClass(), o, fieldName);
    }

    public static<T> T getFieldValue(Class<?> clazz, Object o, String fieldName) {
        return (T) cn.hutool.core.util.ReflectUtil.getFieldValue(o, fieldName);
//        try {
//            // getter方法获取
//            String getterName = "get" + ComUtil.firstUpper(fieldName);
//            Method method = findMethod(clazz, getterName);
//            if (method != null) {
//                method.setAccessible(true);
//                return (T) method.invoke(o);
//            }
//
//            // 直接属性获取值
//            Field field = findField(clazz, fieldName);
//            if (field != null) {
//                field.setAccessible(true);
//                return (T) field.get(o);
//            }
//
//        } catch (IllegalAccessException ignored) {
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
    }

    public static Object getFieldValue(Object o, Field field) {
        return cn.hutool.core.util.ReflectUtil.getFieldValue(o, field);
//        try {
//            field.setAccessible(true);
//            return field.get(o);
//        } catch (IllegalAccessException ignored) {
//            return null;
//        }
    }

    public static Field[] findFields(Object o) {
        return findFields(o.getClass());
    }

    public static Field[] findFields(Class<?> clazz) {
        synchronized (ReflectUtil.FIELDS_CACHE) {
            if (!FIELDS_CACHE.contains(clazz)) {
                // TODO::只能当前类的字段
                FIELDS_CACHE.put(clazz, clazz.getDeclaredFields());
            }
        }
        return FIELDS_CACHE.get(clazz);
    }

    public static Field findField(Object o, String name) {
        return findField(o.getClass(), name);
    }

    public static Field findField(Class<?> clazz, String name) {
        for (Field field : findFields(clazz)) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        if (clazz.getSuperclass() != null) {
            return findField(clazz.getSuperclass(), name);
        }
        return null;
    }

    // 只获取自己的属性
    public static Field findFieldOnly(Class<?> clazz, String name) {
        for (Field field : findFields(clazz)) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 设置属性值，默认走setter，没有则直接设置
     *
     * @param obj     对象
     * @param fieldName 属性名
     * @param value 属性值
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) {
        setFieldValue(obj.getClass(), obj, fieldName, value);
    }

    private static void setFieldValue(Class<?> clazz, Object obj, String fieldName, Object value) {
        Field field = findField(clazz, fieldName);
        if (value != null && field != null && !field.getType().isAssignableFrom(value.getClass())) {
            value = DbClassConv.valueConv(field.getType(), value);
        }
        cn.hutool.core.util.ReflectUtil.setFieldValue(obj, fieldName, value);
//        try {
//            // 走setter设置
//            String setterName = "set" + ComUtil.firstUpper(fieldName);
//            Method method = findMethod(o, setterName, value);
//            if (method != null) {       // java方法是可以被继承的
//                method.invoke(obj, value);
//                return;
//            }
//            // 直接设置属性
//            Field field = findField(clazz, fieldName);
//            if (field != null) {
//                field.setAccessible(true);
//                field.set(o, valueConv(field.getType(), value));
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    public static boolean hasField(Class<?> clazz, String fieldName) {
        return cn.hutool.core.util.ReflectUtil.hasField(clazz, fieldName);
    }

    public static void setFieldValueDirect(Object obj, String fieldName, Object value) {
        setFieldValueDirect(obj.getClass(), obj, fieldName, value);
    }

    public static void setFieldValueDirect(Class<?> clazz, Object obj, String fieldName, Object value) {
        Field field = findField(clazz, fieldName);
        if (value != null && field != null && !field.getType().isAssignableFrom(value.getClass())) {
            value = DbClassConv.valueConv(field.getType(), value);
        }
        cn.hutool.core.util.ReflectUtil.setFieldValue(obj, fieldName, value);
//        try {
//            Field field = findField(clazz, fieldName);
//            if (field != null) {
//                value = valueConv(field.getType(), value);
//                field.setAccessible(true);
//                field.set(obj, value);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void setFieldValue(Object obj, Field field, Object value) {
        cn.hutool.core.util.ReflectUtil.setFieldValue(obj, field, value);
//        try {
//            // setter
//            String setterName = "set" + ComUtil.firstUpper(field.getName());
//            Method method = findMethod(o, setterName, value);
//            if (method != null) {
//                method.setAccessible(true);
//                method.invoke(o, value);
//                return;
//            }
//            // 直接设置
//            field.setAccessible(true);
//            field.set(o, value);
//
//        } catch (Exception e) {
//            System.out.println("设置对象字段数据时异常 o=" + o + "\nfield=" + field + ", value=" + value + "\nmsg=" + e.getMessage());
//            e.printStackTrace();
//        }
    }

    public static Method[] getMethods(Object obj) {
        return getMethods(obj.getClass());
    }

    public static Method[] getMethods(Class<?> clazz) {
        synchronized (METHODS_CACHE) {
            if (!METHODS_CACHE.contains(clazz)) {
                // 注意：这里会获取到继承父类的方法
                METHODS_CACHE.put(clazz, clazz.getDeclaredMethods());
            }
        }
        return METHODS_CACHE.get(clazz);
    }

    public static Method findMethod(Object obj, String name, Object... args) {
        // 参数对象类
        Class<?>[] argsClasses = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                return null;
            }
            argsClasses[i] = args[i] instanceof Class<?> ? (Class<?>) args[i] : args[i].getClass();
        }
        return findMethod(obj, name, argsClasses);
    }

    public static Method findMethod(Object obj, String name, Class<?>... argsClasses) {
        for (Method method : getMethods(obj)) {
            if (method.getName().equals(name) && Arrays.deepEquals(method.getParameterTypes(), argsClasses)) {
                return method;
            }
        }
        return null;
    }

    // FIXME::该方法兼容性差
    public static <T> T invoke(Object obj, String methodName, Object... args)
    {
        return cn.hutool.core.util.ReflectUtil.invoke(obj, methodName, args);
//        try {
//            Method method = findMethod(obj, methodName, args);
//            if (method == null) {
//                return null;
//            }
//            method.setAccessible(true);
//            return (T) method.invoke(obj, args);
//
//        } catch (Exception e) {
////            throw new RuntimeException(e.getMessage());
//            return null;
//        }
    }

    // 格式：
    public static <T> T invoke(String str, Object... args)
    {
        try {
            String[] strArr = str.split("#");
            Class<?> clazz = Class.forName(strArr[0]);
            return ReflectUtil.invoke(clazz, strArr[1], args);
        } catch (Exception e) {
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
