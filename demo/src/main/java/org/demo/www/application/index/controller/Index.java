package org.demo.www.application.index.controller;

import org.quickjava.framework.controller.Controller;

public class Index extends Controller {

    public Object view()
    {
        Index index = new Index();
        return super.view();
    }

    public Object index()
    {
        StringBuffer output = new StringBuffer();
        output.append("<h2>QuickJava page.</h2>");
        output.append("<br>");
        output.append(this.request.query());
        output.append("<br>");
        return output;
    }

    public Object test()
    {
        StringBuffer output = new StringBuffer();
        output.append("Hello, world!");
        output.append("<br>");
        output.append("<h2>This QuickJava.</h2>");
        output.append("<br>");
        output.append(this.request.url);
        output.append("<br>");
        output.append(this.request.method);
        output.append("<br>");
        output.append("domin: " + this.request.domin);
        output.append("<br>");
        output.append(this.request.port);
        output.append("<br>");
        output.append(this.request.path);
        output.append("<br>");
        output.append(this.request.ip);
        output.append("<br>");
        output.append(this.request.moduleName);
        output.append("<br>");
        output.append(this.request.controllerName);
        output.append("<br>");
        output.append(this.request.actionName);
        output.append("<br>");
        return output;
    }

    public Object indexTest()
    {
        return "Hello, indexTest!";
    }

    private Object indexPrivate()
    {
        return "Hello, indexPrivate!";
    }

}
