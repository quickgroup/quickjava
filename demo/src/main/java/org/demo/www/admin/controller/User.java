package org.demo.www.admin.controller;

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
