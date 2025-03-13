package org.quickjava.www.application.admin.controller;

import org.quickjava.web.framework.module.Controller;

public class User extends Controller {


    public Object index()
    {
        return "index";
    }

    public Object login()
    {
        return "login";
    }
}
