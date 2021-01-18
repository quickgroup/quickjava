package org.quickjava.core.bean;

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

    public Dict(Map<String, Object> data) {
        this.data = data;
        // 嵌套MAP
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (Map.class.isAssignableFrom(entry.getValue().getClass())) {
                entry.setValue(new Dict((Map<String, Object>) entry.getValue()));
            }
        }
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
     * {@see #get(String key)}
     * @param key MAP键
     * @return String
     */
    public String getString(String key) {
        checkValueType(data.get(key), String.class);
        return (String) data.get(key);
    }

    public Boolean getBoolean(String key) {
        checkValueType(data.get(key), Boolean.class);
        return (Boolean) data.get(key);
    }

    public Integer getInteger(String key) {
        checkValueType(data.get(key), Integer.class);
        return (Integer) data.get(key);
    }

    public Object getObject(String key) {
        return data.get(key);
    }

    private void checkValueType(Object object, Class clazz)
    {
        if (!(object.getClass().isAssignableFrom(clazz))) {
            throw new ClassCastException("object is not a Dict type, object is " + object.getClass().getTypeName() );
        }
    }

    @Override
    public String toString() {
        return data.toString();
    }

}
