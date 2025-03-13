package org.quickjava.common.utils;

import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Map;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: BeanUtil
 * +-------------------------------------------------------------------
 * Date: 2023-5-29 16:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class BeanUtil {

    public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass, boolean isIgnoreError)
    {
        try {
            T bean = beanClass.newInstance();
            map.forEach((key, val) -> {
                try {
                    String name = key.toString();
                    if (val instanceof Map) {
                        Field field = ReflectUtil.getField(beanClass, name);
                        if (field != null) {
                            Object child = mapToBean((Map<?, ?>) val, field.getDeclaringClass(), isIgnoreError);
                            ReflectUtil.setFieldValue(bean, name, child);
                        }
                    } else {
                        ReflectUtil.setFieldValue(bean, name, val);
                    }
                } catch (Exception e) {
                    if (!isIgnoreError) {
                        throw e;
                    }
                }
            });
            return bean;

        } catch (Exception e) {
            if (!isIgnoreError) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass) {
        return mapToBean(map, beanClass, true);
    }

}
