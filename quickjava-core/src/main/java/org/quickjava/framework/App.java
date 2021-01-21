package org.quickjava.framework;

import org.quickjava.common.Console;
import org.quickjava.common.QLog;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.server.TomcatServer;

import javax.servlet.ServletContext;

/**
 * @author QloPC-Msi
 * @date 2021/01/08
 */
public class App {

    private static App app = new App();

    public static String name = "QuickJava";

    private String basePackages = null;

    private boolean isDebug = false;

    public static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    public static ServletContext servletContext = null;

    public static App get() {
        return app;
    }

    /**
     * 运行方法
     * @param args
     */
    public static void startup(Class applicationClass, String[] args)
    {
        try {
            quickJavaWelcome();

            // 初始化环境
            Env.init(applicationClass);

            // 初始化数据库连接

            // 初始化路由
            Route.init(args);

            // 运行服务器
            app.run(args);

        } catch (Exception exc) {
            QLog.error(exc.getMessage());
            exc.printStackTrace();
        }
    }

    /**
     * @langCn 运行Tomcat
     * @param args
     */
    private void run(String[] args)
            throws Exception
    {
        TomcatServer.run();
    }

    /**
     * @langCn 启动成功
     */
    public static void serverStarting(ServletContext servletContext)
    {
        App.servletContext = servletContext;

        QLog.info("App start complete");
    }

    public static String quickJavaWelcome()
    {
        String strs = "\n" +
                "   ____        _      _        _                  \n" +
                "  / __ \\      (_)    | |      | |                 \n" +
                " | |  | |_   _ _  ___| | __   | | __ ___   ____ _ \n" +
                " | |  | | | | | |/ __| |/ /   | |/ _` \\ \\ / / _` |\n" +
                " | |__| | |_| | | (__|   < |__| | (_| |\\ V / (_| |\n" +
                "  \\___\\_\\\\__,_|_|\\___|_|\\_\\____/ \\__,_| \\_/ \\__,_|\n";
        strs += " :: " + Console.Color.output("QuickJava", "INFO") + " :: " + QuickJavaBoot.version + " ("+ QuickJavaBoot.versionCode +") \n";
        System.out.println(strs);
        return strs;
    }

    /**
     * @langCn 线程存值
     */
    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<Object>(){
        @Override
        protected Object initialValue()
        {
            System.out.println("调用get方法时，当前线程共享变量没有设置，调用initialValue获取默认值！");
            return null;
        }
    };

    public static void setCurrentRequest(Request request) {
        threadLocal.set(request);
    }

    public static Request getCurrentRequest() {
        return (Request) threadLocal.get();
    }

}
