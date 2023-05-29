package org.quickjava.common;

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

    public static <T> T mapToBeanIgnoreCase(Map<?, ?> map, Class<T> beanClass, boolean isIgnoreError) {

    }

    public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass) {
        return mapToBeanIgnoreCase(map, beanClass, true);
    }

    public static <T> T toBeanIgnoreCase(Object source, Class<T> clazz, boolean ignoreError) {

    }

}
