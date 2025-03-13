package org.quickjava.www.application.index.controller;

import org.quickjava.web.framework.module.Controller;

public class Index extends Controller {

    public Object index()
    {
        return render();
    }

    public Object about()
    {
        return render();
    }

    public Object nulls()
    {
        return null;
    }

}
