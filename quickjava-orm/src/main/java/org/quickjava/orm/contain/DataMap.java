package org.quickjava.orm.contain;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: DataMap
 * +-------------------------------------------------------------------
 * Date: 2023-3-1 11:23
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 * example:
 * new DataMap().put("userId", entity.getUserId()).put("postId", postId)
 */
public class DataMap extends LinkedHashMap<String, Object> implements Map<String, Object> {

    public DataMap() {
    }

    public static DataMap one() {
        return new DataMap();
    }

    public static DataMap one(String key, Object val) {
        return new DataMap().put(key, val);
    }

    public DataMap put(String key, Object val) {
        super.put(key, val);
        return this;
    }

    public DataMap getMap(String key) {
        return (DataMap) get(key);
    }

}
