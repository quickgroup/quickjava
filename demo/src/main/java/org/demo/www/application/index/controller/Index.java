package org.demo.www.application.index.controller;

import org.quickjava.framework.controller.Controller;

public class Index extends Controller {

    public Object index()
    {
        StringBuffer output = new StringBuffer();
        output.append("Hello, world!");
        output.append("<br>");
        output.append(this.request.getUrl());
        output.append("<br>");
        output.append(this.request.getMethod());
        output.append("<br>");
        output.append("domin: " + this.request.getDomin());
        output.append("<br>");
        output.append(this.request.getPort());
        output.append("<br>");
        output.append(this.request.getPath());
        output.append("<br>");
        output.append(this.request.getIp());
        output.append("<br>");
        output.append(this.request.getPathinfo().module);
        output.append("<br>");
        output.append(this.request.getPathinfo().controller);
        output.append("<br>");
        output.append(this.request.getPathinfo().action);
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
