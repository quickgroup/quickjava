package org.quickjava.orm.utils;

import cn.hutool.core.util.ObjectUtil;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: HelperController
 * +-------------------------------------------------------------------
 * Date: 2023-3-14 11:52
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public abstract class Helper {

    public static boolean isEmpty(Object obj) {
        return ObjectUtil.isEmpty(obj);
    }

    public static boolean isNotEmpty(Object obj) {
        return ObjectUtil.isNotEmpty(obj);
    }

}
