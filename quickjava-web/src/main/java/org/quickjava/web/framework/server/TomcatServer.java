package org.quickjava.web.framework.server;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.quickjava.web.common.QuickUtil;
import org.quickjava.web.framework.Kernel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.File;

public class TomcatServer {
    private static final Logger logger = LoggerFactory.getLogger(TomcatServer.class);

    private static String contextPath = "";

    public static void run()
            throws LifecycleException, ServletException
    {
        Tomcat tomcat = new Tomcat();

        logger.info("Starting Tomcat.");

        /*
         * #quickLang 配置tomcat主机名和端口
         */
        tomcat.setHostname(Kernel.config().getDict("app").getString("hostname", "localhost"));
        tomcat.setPort(Kernel.config().getDict("app").getInteger("port", 8700));
        tomcat.getConnector().setUseIPVHosts(true);
        logger.info("Server Listen: "
                + Kernel.config().getDict("app").getString("hostname", "localhost")
                + ":"
                + Kernel.config().getDict("app").getInteger("port", 8700));

        String classesPath = (QuickUtil.isClassMode()) ? QuickUtil.getClassesPath() : "";
        String webInfClasses = "/WEB-INF/classes";

        // new Context
        StandardContext ctx = (StandardContext) tomcat
                .addWebapp(contextPath, new File(classesPath).getAbsolutePath());
        ctx.setSessionCookieName("QSESSION");

        // Configure the resource path
        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, webInfClasses, classesPath, "/"));
        ctx.setResources(resources);

        // Configure Servlet
        tomcat.addServlet(contextPath, "quickServlet", new QuickServlet());
        ctx.addServletMappingDecoded("/*", "quickServlet");

        // static Servlet
        tomcat.addServlet(contextPath, "quickDefaultServlet", new QuickDefaultServlet());
        ctx.addServletMappingDecoded("/static/*", "quickDefaultServlet");

        // 自定义错误页面：404、500等被tomcat处理的异常
//        new ErrorPage()
//        ctx.addErrorPage();

        // tomcat log level
        java.util.logging.Logger.getLogger("org.apache").setLevel(java.util.logging.Level.WARNING);

        // start tomcat
        tomcat.start();

        // App global servletContext
        Kernel.serverStarting(ctx.getServletContext());

        // start success
        tomcat.getServer().await();
    }

}
