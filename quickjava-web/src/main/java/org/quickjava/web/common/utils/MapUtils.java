package org.quickjava.web.common.utils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 14:36
 */
public class MapUtils {

    /**
     * Map数据转对象
     * @param clazz
     * @param map
     * @return Object
     * @throws Exception
     */
    public static Object map2Bean(Class<?> clazz, Map<String, Object> map)
            throws Exception
    {
        Object javabean = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            if (map.containsKey(name)) {
                /**
                 * 依赖对象属性的类型作为强转目标类型
                 */
                field.set(javabean, fieldValueOf(field.getType(), map.get(name)));
            }
        }

        return javabean;
    }

    private static Object fieldValueOf(Class objectClazz, Object value)
    {
        String className = objectClazz.getTypeName();
        if (className.equals(Boolean.class.getTypeName())) {
            return Boolean.valueOf(value.toString());
        }
        else if (className.equals(String.class.getTypeName())) {
            return String.valueOf(value.toString());
        }
        else if (className.equals(Integer.class.getTypeName())) {
            return Integer.valueOf(value.toString());
        }
        else if (className.equals(Double.class.getTypeName())) {
            return Double.valueOf(value.toString());
        }

        return null;
    }
}
