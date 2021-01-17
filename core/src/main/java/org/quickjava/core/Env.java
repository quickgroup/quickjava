package org.quickjava.core;

import org.quickjava.core.annotation.QuickBoot;
import org.quickjava.core.exception.MapNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * 环境变量管理
 */
public class Env {

    private static Map<String, Object> data = new HashMap<String, Object>();

    public static void init(Class applicationClass)
            throws Exception
    {
        // 根目录
        set("ROOT_PATH", System.getProperty("user.dir"));
        // 项目包
        QuickBoot quickBoot = applicationClass.newInstance().getClass().getAnnotation(QuickBoot.class);
        set("basePackages", quickBoot.value());
    }

    /**
     * 获取变量值
     * @param name
     * @return Object
     */
    public static Object get(String name)
    {
        if (data.containsKey(name)) {
            return data.get(name);
        }
        throw new MapNotFoundException(name + " key not found.");
    }

    public static Object getOrDefault(String name, Object object)
    {
        if (data.containsKey(name)) {
            return data.get(name);
        }
        return object;
    }

    public static String getString(String name)
    {
        return (String) get(name);
    }

    public static String getStringOrDefault(String name, String value)
    {
        return (String) getOrDefault(name, value);
    }

    /**
     * 获取变量值
     * @param name
     * @return
     */
    public static void set(String name, Object object)
    {
        data.put(name, object);
    }
}
