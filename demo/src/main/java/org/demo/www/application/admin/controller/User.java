package org.demo.www.application.admin.controller;

import org.quickjava.framework.controller.Controller;

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
