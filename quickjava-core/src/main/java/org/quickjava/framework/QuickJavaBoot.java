package org.quickjava.framework;

/**
 * @author QloPC-Msi
 * @date 2021/0108
 */
public class QuickJavaBoot {

    /**
     * @langCn 版本号
     */
    public static final String version = "1.0-SNAPSHOT";

    /**
     * @langCn 版本号值
     */
    public static final Integer versionCode = 2101001;

    /**
     * @langCn 启动入口
     * @param args arguments
     */
    public static void start(Class clazz, String[] args)
    {
        App.startup(clazz, args);
    }

}