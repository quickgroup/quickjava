package org.quickjava.framework.server;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.quickjava.common.QuickLog;
import org.quickjava.common.QuickUtil;
import org.quickjava.framework.Kernel;

import javax.servlet.ServletException;
import java.io.File;

public class TomcatServer {

    private static String contextPath = "";

    public static void run()
            throws LifecycleException, ServletException
    {
        Tomcat tomcat = new Tomcat();

        QuickLog.info("Starting Tomcat.");

        /**
         * #quickLang 配置tomcat主机名和端口
         */
        tomcat.setHostname(Kernel.config().get("app").getString("hostname", "localhost"));
        tomcat.setPort(Kernel.config().get("app").getInteger("port", 8700));
        tomcat.getConnector().setUseIPVHosts(true);
        QuickLog.info("Server Listen: "
                + Kernel.config().get("app").getString("hostname", "localhost")
                + ":"
                + Kernel.config().get("app").getInteger("port", 8700));

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
