package org.quickjava.framework;

/**
 * @author QloPC-Msi
 * @date 2021/0108
 */
public class QuickJavaBoot {

    /**
     * #quickLang 版本号
     */
    public static final String version = "0.1.21.1117-dev";

    /**
     * #quickLang 版本号值
     */
    public static final Integer versionCode = 211228;

    /**
     * #quickLang 启动入口
     * @param args arguments
     */
    public static void start(Class clazz, String[] args)
    {
        Kernel.startup(clazz, args);
    }

}
