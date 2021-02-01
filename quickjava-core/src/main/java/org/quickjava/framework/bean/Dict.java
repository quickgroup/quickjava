package org.quickjava.framework.bean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/18 22:40
 * @projectName quickjava
 */
public class Dict {

    /**
     * 真实存放数据
     */
    private Map<String, Object> data = new LinkedHashMap<>();

    public Dict() {}

    public Dict(Map data)
    {
        Map<String, Object> data1 = data;
        for (Map.Entry<String, Object> entry : data1.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
                entry.setValue(new Dict((Map) entry.getValue()));
            }
        }

        this.data = data;
    }

    public Dict set(String key, Object object)
    {
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

        this.data.put(key, object);

        return this;
    }

    /**
     * @param key MAP键
     * @return Dict
     */
    public Dict get(String key) {
        checkValueType(data.get(key), Dict.class);
        return (Dict) data.get(key);
    }

    /**
     * {@see #_get(String key)}
     * @param key MAP键
     * @return String
     */
    public String getString(String key) {
        return (String) data.get(key);
    }

    public String getString(String key, String defaultValue) {
        return (data.get(key) != null) ? (String) data.get(key) : defaultValue;
    }

    public Boolean getBoolean(String key) {
        checkValueType(data.get(key), Boolean.class);
        return (Boolean) data.get(key);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return (data.get(key) != null) ? (Boolean) data.get(key) : defaultValue;
    }

    public Integer getInteger(String key) {
        checkValueType(data.get(key), Integer.class);
        return (Integer) data.get(key);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return (data.get(key) != null) ? (Integer) data.get(key) : defaultValue;
    }

    public Object getObject(String key) {
        return data.get(key);
    }

    public Object getObject(String key, Object defaultValue) {
        return (data.get(key) != null) ? data.get(key) : defaultValue;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    private void checkValueType(Object object, Class clazz)
    {
        if (!(object.getClass().isAssignableFrom(clazz))) {
            throw new ClassCastException("object is not a Dict type, object is " + object.getClass().getTypeName() );
        }
    }

    /**
     *
     * @param dst
     * @param src
     * @return
     */
    public static Dict putAll(Dict dst, Dict src)
    {
        return new Dict(mapPutAll(dst.getData(), src.getData()));
    }

    private static Map<String, Object> mapPutAll(Map<String, Object> dstMap, Map<String, Object> srcMap)
    {
        for (Map.Entry<String, Object> entry : dstMap.entrySet()) {

            Object value = entry.getValue();
            Object valueSrc = srcMap.get(entry.getKey());

            if (value == null) {    // 默认配置都没有，就跳过
                dstMap.put(entry.getKey(), entry.getValue());
                continue;
            }

            if (value instanceof Dict && (valueSrc != null && valueSrc instanceof Dict)) {
                Map<String, Object> childDataDst = ((Dict) value).getData();
                Map<String, Object> childDataSrc = ((Dict) valueSrc).getData();
                dstMap.put(entry.getKey(), mapPutAll( childDataDst, childDataSrc));
//                System.out.println("value is dict.");
            }
            else if (Map.class.isAssignableFrom(value.getClass()) && (valueSrc != null && valueSrc instanceof Map)) {
                Map<String, Object> childDataDst = (Map<String, Object>) value;
                Map<String, Object> childDataSrc = (Map<String, Object>) valueSrc;
                dstMap.put(entry.getKey(), mapPutAll( childDataDst, childDataSrc));
//                System.out.println("value is Map.");
            }
            else {
                dstMap.put(entry.getKey(), valueSrc);
            }
        }
        return dstMap;
    }

    @Override
    public String toString() {
        return data.toString();
    }

}
