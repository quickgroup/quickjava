package org.quickjava.common.utils;

/*
 * Copyright (c) 2020~2022 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: GenericsUtils
 * +-------------------------------------------------------------------
 * Date: 2022-7-25 19:35
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericsUtils {

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型
     * @param clazz 类
     * @return 返回第一个类型
     */
    public static Class<?> getSuperClassGenericsType(Class<?> clazz) {
        return getSuperClassGenericsType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型
     * @param clazz 类
     * @param index 返回某下标的类型
     * @return 返回泛型实际类型
     */
    public static Class<?> getSuperClassGenericsType(Class<?> clazz, int index)
            throws IndexOutOfBoundsException {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }
}
