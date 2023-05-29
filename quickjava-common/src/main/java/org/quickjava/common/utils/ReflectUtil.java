package org.quickjava.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
}
