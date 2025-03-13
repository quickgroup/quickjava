/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Dict.java
 * +-------------------------------------------------------------------
 * Date: 2021/06/17 14:41:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.framework.bean;

import java.util.LinkedHashMap;
import java.util.Map;

public class Dict extends LinkedHashMap<String, Object> {

    public Dict() {
    }

    public Dict(Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
                entry.setValue(new Dict((Map<String, Object>) entry.getValue()));
            }
        }
        this.putAll(data);
    }

//    public Dict(Object data) {
//
//    }

    public Dict set(String key, Object object) {
        if (Map.class.isAssignableFrom(object.getClass())) {
            Map<String, Object> objectMap = (Map) object;
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
                    entry.setValue(new Dict((Map) entry.getValue()));
                }
            }
        }
        this.put(key, object);
        return this;
    }

    /**
     * @param key MAP键
     * @return Dict
     */
    public Dict getDict(String key) {
        Object value = get(key);
        return value instanceof Dict ? (Dict) value : null;
    }

    /**
     * {@see #_get(String key)}
     *
     * @param key MAP键
     * @return String
     */
    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        Object value = containsKey(key) ? get(key) : null;
        return value == null ? defaultValue : String.valueOf(value);
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        Object value = containsKey(key) ? get(key) : null;
        return value == null ? defaultValue : (Boolean) value;
    }

    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        Object value = containsKey(key) ? get(key) : null;
        return value == null ? defaultValue : (Integer) value;
    }

    private void checkValueType(Object object, Class<?> clazz) {
        if (object != null && !(object.getClass().isAssignableFrom(clazz))) {
            throw new ClassCastException("object is not a Dict type, object is " + object.getClass().getTypeName());
        }
    }

    public static Dict merge(Dict dst, Dict src) {
        return new Dict(mapPutAll(dst, src));
    }

    private static Map<String, Object> mapPutAll(Map<String, Object> dst, Map<String, Object> src) {
        for (Map.Entry<String, Object> entry : dst.entrySet()) {
            Object dstValue = entry.getValue();
            Object srcValue = src.get(entry.getKey());
            if (dstValue == null) {    // 默认配置都没有，就跳过
                dst.put(entry.getKey(), srcValue);
                continue;
            }

            if (Map.class.isAssignableFrom(dstValue.getClass()) && (srcValue != null && srcValue instanceof Map)) {
                if (srcValue != null && Map.class.isAssignableFrom(srcValue.getClass())) {
                    Map<String, Object> childDataDst = (Map<String, Object>) dstValue;
                    Map<String, Object> childDataSrc = (Map<String, Object>) srcValue;
                    dst.put(entry.getKey(), mapPutAll(childDataDst, childDataSrc));
                }
            } else {
                dst.put(entry.getKey(), srcValue);
            }
        }
        // src to dst
        for (Map.Entry<String, Object> entry : src.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
                if (!dst.containsKey(entry.getKey())) {
                    dst.put(entry.getKey(), new LinkedHashMap<>());
                }
                if (Map.class.isAssignableFrom(dst.get(entry.getKey()).getClass())) {
                    Map<String, Object> childDataDst = (Map<String, Object>) dst.get(entry.getKey());
                    dst.put(entry.getKey(), mapPutAll(childDataDst, (Map<String, Object>) entry.getValue()));
                }
            } else {
                dst.put(entry.getKey(), entry.getValue());
            }
        }
        return dst;
    }

}

