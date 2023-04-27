package org.quickjava.framework.orm.utils;

import net.sf.cglib.proxy.Enhancer;
import org.quickjava.framework.orm.Model;
import org.quickjava.framework.orm.contain.ModelField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelUtil
 * +-------------------------------------------------------------------
 * Date: 2023-3-10 17:15
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class ModelUtil extends SqlUtil {

    public static final Map<Class<?>, Map<String, ModelField>> __fieldCache = new LinkedHashMap<>();
    // 这个map只缓存是关联属性的方法
    public static final Map<Class<?>, Map<String, ModelField>> __methodCache = new LinkedHashMap<>();

    public static void putFieldMap(Class<?> clazz, Map<String, ModelField> fieldMap) {
        ModelUtil.__fieldCache.put(clazz, fieldMap);
    }

    public static Map<String, ModelField> findFieldMap(Class<?> clazz) {
        return ModelUtil.__fieldCache.get(clazz);
    }

    public static ModelField findField(Class<?> clazz, String name) {
        Map<String, ModelField> fieldMap = findFieldMap(clazz);
        return fieldMap == null ? null : fieldMap.get(name);
    }

    public static boolean existFieldMap(Class<?> clazz) {
        return ModelUtil.__fieldCache.containsKey(clazz);
    }

    public static void putMethodMap(Class<?> clazz, Map<String, ModelField> methodMap) {
        ModelUtil.__methodCache.put(clazz, methodMap);
    }

    public static Map<String, ModelField> findMethodMap(Class<?> clazz) {
        return ModelUtil.__methodCache.get(clazz);
    }

    public static ModelField findMethod(Class<?> clazz, Method method) {
        return findMethod(clazz, method.getName());
    }

    public static ModelField findMethod(Class<?> clazz, String name) {
        Map<String, ModelField> ret = findMethodMap(clazz);
        return ret == null ? null : ret.get(name);
    }

    public static boolean existMethod(Class<?> clazz, String name) {
        Map<String, ModelField> ret = findMethodMap(clazz);
        return ret != null && ret.get(name) != null;
    }

    public static boolean isProxyModel(Class<?> clazz) {
        return Enhancer.isEnhanced(clazz);
    }

    public static boolean isProxyModel(Object obj) {
        return Enhancer.isEnhanced(obj.getClass());
    }

    public static boolean isVegetarianModel(Class<?> clazz) {
        return !isProxyModel(clazz);
    }

    public static boolean isVegetarianModel(Object obj) {
        return !isProxyModel(obj.getClass());
    }

    public static Class<?> getModelClass(Class<?> clazz) {
        return Enhancer.isEnhanced(clazz) ? clazz.getSuperclass() : clazz;
    }

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
        }
    }

    public static void setFieldValue(Object o, Field field, Object value)
    {
        try {
            field.setAccessible(true);
            // 实现对关联属性的数据赋值
            if (Model.class.isAssignableFrom(field.getType())) {
                if (value instanceof Map) {
                    Model child = Model.newProxyModel(field.getType(), (Map<String, Object>) value, (Model) o);
                    field.set(o, child);
                }
            } else {
                SqlUtil.setFieldValue(o, field, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
