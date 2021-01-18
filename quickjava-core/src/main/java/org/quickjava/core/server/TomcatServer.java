package org.quickjava.core.server;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.ServletException;
import java.io.File;

public class TomcatServer {

    public static void run()
            throws LifecycleException, ServletException
    {
        Tomcat tomcat = new Tomcat();
//        tomcat.setHostname("127.0.0.1");
        tomcat.setPort(8080);

        // 设置 Context
        StandardContext ctx = (StandardContext) tomcat
                .addWebapp("", new File("target/").getAbsolutePath());

        // 配置 Context 资源路径
        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        // 配置 Servlet
        tomcat.addServlet("", "tomcatServlet", new TomcatServlet());
        ctx.addServletMappingDecoded("/*", "tomcatServlet");

        tomcat.start();
        tomcat.getServer().await();
    }

}
