package org.quickjava.web.framework;

import org.quickjava.web.common.Console;
import org.quickjava.web.common.utils.FileUtils;
import org.quickjava.web.database.QuickJavaDB;
import org.quickjava.web.framework.bean.Dict;
import org.quickjava.web.framework.config.AppConfig;
import org.quickjava.web.framework.http.Request;
import org.quickjava.web.framework.server.TomcatServer;
import org.quickjava.web.framework.view.ViewMan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Qlo1062 [ QloPC-zs ]
 */
public class Kernel {
    private static final Logger logger = LoggerFactory.getLogger(Kernel.class);

    private static final Kernel kernel = new Kernel();

    public static String name = "QuickJava";

    private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    public static ServletContext servletContext = null;

    private static Dict config = null;

    public static Kernel get() {
        return kernel;
    }

    /**
     * 运行方法
     */
    public static void startup(Class<?> applicationClass, String[] args)
    {
        try {
            // 初始化
            kernel.init(applicationClass, args);
            // 运行服务器
            kernel.run(args);
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    public static void init(Class<?> applicationClass, String[] args)
    {
        try {
            kernel.welcome();
            logger.info(Lang.to("App start ..."));

            // 环境
            Env.init(applicationClass);

            // 框架配置
            kernel.loadConfig();

            // 语言包
            Lang.init();

            // 钩子
            Hook.init();
            Hook.call("app_init");

            // 缓存
            Cache.init();

            // 路由
            Route.init();

            // 视图初始化
            ViewMan.init();

            // 数据库
            new QuickJavaDB().init();

            Hook.call("app_init_complete");

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    /**
     * #quickLang 运行Tomcat
     */
    private void run(String[] args)
            throws Exception
    {
        TomcatServer.run();
    }

    /**
     * #quickLang 启动成功
     */
    public static void serverStarting(ServletContext servletContext)
    {
        Kernel.servletContext = servletContext;
        Hook.call("app_start_complete");
        logger.info(Lang.to("App start complete"));
    }

    /**
     * #quickLang 加载配置文件
     */
    public void loadConfig() throws Exception
    {
        Class<?> applicationClass = Env.getApplicationClass();
        try (InputStream in = applicationClass.getClassLoader().getResourceAsStream("application.yml")) {
            String content = FileUtils.getInputStreamContent(in);
            config = AppConfig.Factory.loadFormYml(content);
            logger.debug(Lang.to("Config load Complete."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Dict config() {
        return config;
    }

    public static ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * #quickLang welcome
     * @return
     */
    public String welcome()
    {
        String strs = "\n" +
                "   ____        _      _        _                  \n" +
                "  / __ \\      (_)    | |      | |                 \n" +
                " | |  | |_   _ _  ___| | __   | | __ ___   ____ _ \n" +
                " | |  | | | | | |/ __| |/ /   | |/ _` \\ \\ / / _` |\n" +
                " | |__| | |_| | | (__|   < |__| | (_| |\\ V / (_| |\n" +
                "  \\___\\_\\\\__,_|_|\\___|_|\\_\\____/ \\__,_| \\_/ \\__,_|\n";
        strs += "\t" + Console.Color.output("QuickJava", "INFO") + "\t" + QuickJavaBoot.version + " ("+ QuickJavaBoot.versionCode +") \n";
        System.out.println(strs);
        return strs;
    }

    /**
     * #quickLang 线程存值
     */
    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<Object>(){
        @Override
        protected Object initialValue()
        {
            System.out.println("initialValue 设置默认值！");
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
