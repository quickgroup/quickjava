package org.quickjava.core;

import org.quickjava.core.server.TomcatServer;

public class App {

    private static App app = new App();

    private String basePackages = null;

    private boolean isDebug = false;

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
