/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.www.application.api.controller;

import org.quickjava.web.framework.module.Controller;

public class Index extends Controller {

    public Object index()
    {
        StringBuffer output = new StringBuffer();
        output.append("module: " + this.request.moduleName );
        output.append("<br>");
        output.append("class: " + this.name );
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
