package org.quickjava.web.framework;

import org.quickjava.web.common.QuickUtil;
import org.quickjava.web.framework.annotation.ApplicationQuickBoot;
import org.quickjava.web.framework.exception.MapNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * #quickLang 环境变量管理
 * @author QloPC-Msi
 */
public class Env {
    private static final Logger logger = LoggerFactory.getLogger(Env.class);

    private static Class<?> applicationClass = null;
    private static final Map<String, Object> data = new HashMap<>();

    public static void init(Class<?> applicationClass)
            throws Exception
    {
        // 应用的启动类（读取配置等使用
        Env.applicationClass = applicationClass;
        // 根目录
        set("rootPath", QuickUtil.getRootPath());
        set("classPath", applicationClass.getClassLoader().getResource("").getPath());
        // 项目包
        ApplicationQuickBoot quickBoot = applicationClass.getAnnotation(ApplicationQuickBoot.class);
        String basePackages = "".equals(quickBoot.value()) ? applicationClass.getPackage().getName() + ".application" : quickBoot.value();
        set("basePackages", basePackages);

        Env.systemEnvInit();

        logger.debug(Lang.to("Env init Complete."));
    }

    public static Class<?> getApplicationClass() {
        return applicationClass;
    }

    public static void systemEnvInit()
    {
        // IPV4
        Properties properties = System.getProperties();
        properties.setProperty("java.net.preferIPv4Stack", "true");
    }

    /**
     * 获取变量值
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
     */
    public static void set(String name, Object object)
    {
        data.put(name, object);
    }
}
