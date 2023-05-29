package org.quickjava.orm.utils;

import net.sf.cglib.proxy.Enhancer;
import org.quickjava.orm.Model;
import org.quickjava.orm.contain.ModelMeta;

import java.lang.reflect.Field;
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

    public static final Map<Class<?>, ModelMeta> modelCache = new LinkedHashMap<>();

    public static ModelMeta getMeta(Class<?> clazz) {
        return modelCache.get(clazz);
    }

    public static void setMeta(Class<?> clazz, ModelMeta meta) {
        modelCache.put(clazz, meta);
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

    public static Class<?> getModelClass(Object obj) {
        if (obj instanceof Class) {
            return getModelClass((Class<?>) obj);
        }
        return getModelClass(obj.getClass());
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

    /**
     * 直接拷贝属性
     * @param src 来源对象
     * @param dst 目标对象
     */
    public static void copyProperties(Object src, Object dst)
    {
        try {
            Class<?> srcClass = getModelClass(src);
            Class<?> dstClass = getModelClass(dst);
            if (Map.class.isAssignableFrom(dstClass)) {
                throw new RuntimeException("暂不支持 dst 为Map的拷贝");
            }
            // map=>实体
            if (Map.class.isAssignableFrom(srcClass)) {
                Map<String, Object> data = (Map<String, Object>) src;
                for (Field field : dstClass.getDeclaredFields()) {
                    if (data.containsKey(field.getName())) {
                        field.setAccessible(true);
                        field.set(dst, data.get(field.getName()));
                        continue;
                    }
                    String fieldName = ModelUtil.toCamelCase(field.getName());
                    if (data.containsKey(fieldName)) {
                        field.setAccessible(true);
                        field.set(dst, data.get(fieldName));
                        continue;
                    }
                    fieldName = ModelUtil.toUnderlineCase(field.getName());
                    if (data.containsKey(fieldName)) {
                        field.setAccessible(true);
                        field.set(dst, data.get(fieldName));
                        continue;
                    }
                }
                return;
            }
            // 实体=>实体
            Map<String, Object> srcMap = new LinkedHashMap<>();
            for (Field field : srcClass.getDeclaredFields()) {
                field.setAccessible(true);
                srcMap.put(field.getName(), field.get(src));
            }
            for (Field field : dstClass.getDeclaredFields()) {
                if (srcMap.containsKey(field.getName())) {
                    field.setAccessible(true);
                    field.set(dst, srcMap.get(field.getName()));
                }
            }
        } catch (IllegalAccessException ignored) {
        }
    }

    /**
     * Java程序数据转sql兼容数据，如：true=1、false=0
     * @param val 值
     * @return 转换后的值
     */
    public static Object valueToSqlValue(Object val)
    {
        if (val instanceof Boolean) {
            return Boolean.TRUE.equals(val) ? 1 : 0;
        }
        return val;
    }

    /**
     * 对象比较
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 比较结果
     */
    public static boolean objectEquals(Object obj1, Object obj2)
    {
        if (obj1 == null && obj2 == null) {
            return true;
        } else if (obj1 != null) {
            return obj1.equals(obj2);
        } else if (obj2 != null) {
            return obj2.equals(obj1);
        } else {
            return false;
        }
    }

}
