package org.quickjava.framework.server;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.quickjava.common.QLog;
import org.quickjava.common.QUtils;

import javax.servlet.ServletException;
import java.io.File;

public class TomcatServer {

    public static void run()
            throws LifecycleException, ServletException
    {
        Tomcat tomcat = new Tomcat();
//        tomcat.setHostname("127.0.0.1");
        tomcat.setPort(8080);

        String classesPath = QUtils.getClassesPath();
        String webInfClasses = "/WEB-INF/classes";

        // new Context
        StandardContext ctx = (StandardContext) tomcat
                .addWebapp("", new File(classesPath).getAbsolutePath());

        // Configure the resource path
        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, webInfClasses, classesPath, "/"));
        ctx.setResources(resources);

        // Configure Servlet
        tomcat.addServlet("", "tomcatServlet", new TomcatServlet());
        ctx.addServletMappingDecoded("/*", "tomcatServlet");

        //
        java.util.logging.Logger.getLogger("org.apache").setLevel(java.util.logging.Level.WARNING);

        // start tomcat
        QLog.info("start tomcat");
        tomcat.start();

        // start success
        QLog.info("start tomcat complete");
        tomcat.getServer().await();
    }

}
