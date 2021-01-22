package org.quickjava.framework.server;

import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.HostConfig;
import org.apache.catalina.startup.HostRuleSet;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.quickjava.common.QLog;
import org.quickjava.common.QUtils;
import org.quickjava.framework.App;

import javax.servlet.ServletException;
import java.io.File;
import java.util.Properties;

public class TomcatServer {

    public static void run()
            throws LifecycleException, ServletException
    {
        // IPV4
        Properties properties = System.getProperties();
        properties.setProperty("java.net.preferIPv4Stack", "true");

        Tomcat tomcat = new Tomcat();
        /**
         * @langCn configure tomcat hostname and port
         */
        tomcat.setHostname(App.config.get("app").getString("hostname", "localhost"));
        tomcat.setPort(App.config.get("app").getInteger("port", 8080));
        tomcat.getConnector().setUseIPVHosts(true);

        QLog.info("Server Listen: "
                + App.config.get("app").getString("hostname", "localhost")
                + ":"
                + App.config.get("app").getInteger("port", 8080));

        String classesPath = (QUtils.isClassMode()) ? QUtils.getClassesPath() : "";
        String webInfClasses = "/WEB-INF/classes";

        // new Context
        StandardContext ctx = (StandardContext) tomcat
                .addWebapp("", new File(classesPath).getAbsolutePath());
        ctx.setSessionCookieName("QSESSION");

        // Configure the resource path
        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, webInfClasses, classesPath, "/"));
        ctx.setResources(resources);

        // Configure Servlet
        tomcat.addServlet("", "tomcatServlet", new TomcatServlet());
        ctx.addServletMappingDecoded("/*", "tomcatServlet");

        // tomcat log level
        java.util.logging.Logger.getLogger("org.apache").setLevel(java.util.logging.Level.WARNING);

        // start tomcat
        tomcat.start();
        // App global servletContext
        App.serverStarting(ctx.getServletContext());

        // start success
        tomcat.getServer().await();
    }

}
