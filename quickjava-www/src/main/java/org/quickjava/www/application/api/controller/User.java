/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.www.application.api.controller;

import org.quickjava.web.framework.module.Controller;

public class User extends Controller {

    public Object register()
    {
        return "register";
    }

    public Object login()
    {
        return "login";
    }
}
