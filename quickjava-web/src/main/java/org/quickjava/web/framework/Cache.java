/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Cache.java
 * +-------------------------------------------------------------------
 * Date: 2021/11/17 10:05:22
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.web.framework;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cache {

    private static String handler = "memory";   // 缓存引擎

    private static Integer timeout = 3600;   // 缓存超时

    private static Map<Object, Object> data = null;

    public static void init() {
        handler = Kernel.config().getDict("cache").getString("handler");
        timeout = Kernel.config().getDict("cache").getInteger("timeout");
        data = new LinkedHashMap<>();
    }

    public static Object get(String key) {
        return data.get(key);
    }

    public static String getString(String key) {
        return data.containsKey(key) ? String.valueOf(data.get(key)) : null;
    }

    public static String getInt(String key) {
        return null;
    }

    public static String getFile(String key) {
        return null;
    }

    public static void set(String key, Object value, Integer timeout) {
        data.put(key, value);
    }

    public static void set(String key, Object value) {
        set(key, value, timeout);
    }

    public static void setFile(String key, Object value) {
        set(key, value, timeout);
    }



}
