package org.quickjava.orm.utils;

/*
 * Copyright (c) 2020~2025 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: BeanUtils
 * +-------------------------------------------------------------------
 * Date: 2025/3/18 15:51
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {

    /**
     * 通过反射调用对象的 getter 方法获取属性值
     *
     * @param obj        目标对象
     * @param propertyName 属性名（支持嵌套属性，例如 "user.address.city"）
     * @return 属性值
     * @throws Exception 如果属性不存在或无法访问
     */
    public static Object getPropertyValue(Object obj, String propertyName) throws Exception {
        if (obj == null || propertyName == null || propertyName.isEmpty()) {
            throw new IllegalArgumentException("对象和属性名不能为空");
        }

        // 拆分嵌套属性（例如 "user.address.city"）
        String[] properties = propertyName.split("\\.");
        Object currentObj = obj;

        // 遍历嵌套属性
        for (String prop : properties) {
            currentObj = invokeGetter(currentObj, prop);
            if (currentObj == null) {
                break; // 如果中间某个属性为 null，直接返回 null
            }
        }

        return currentObj;
    }

    /**
     * 调用单个属性的 getter 方法
     *
     * @param obj        目标对象
     * @param propertyName 属性名
     * @return 属性值
     * @throws Exception 如果属性不存在或无法访问
     */
    private static Object invokeGetter(Object obj, String propertyName) throws Exception {
        // 构造 getter 方法名（例如 "getName"）
        String getterName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

        try {
            // 获取 getter 方法
            Method getter = obj.getClass().getMethod(getterName);
            // 调用 getter 方法并返回值
            return getter.invoke(obj);
        } catch (NoSuchMethodException e) {
            throw new Exception("属性 " + propertyName + " 的 getter 方法不存在", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new Exception("无法访问属性 " + propertyName + " 的 getter 方法", e);
        }
    }

}
