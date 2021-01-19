package org.quickjava.core;

import org.quickjava.core.server.TomcatServer;

public class App {

    private static App app = new App();

    public static String name = "QuickJava";

    private String basePackages = null;

    private boolean isDebug = false;

    public static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    /**
     * 运行方法
     * @param args
     */
    public static void startup(Class applicationClass, String[] args)
    {
        try {

            // 初始化环境
            Env.init(applicationClass);

            // 初始化数据库连接

            // 初始化路由
            Route.init(args);

            // 运行服务器
            app.run(args);

        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            exc.printStackTrace();
        }
    }

    /**
     * 运行Tomcat
     * @param args
     */
    private void run(String[] args)
            throws Exception
    {
        TomcatServer.run();
    }

}
