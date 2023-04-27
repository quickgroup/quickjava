/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Verify.java
 * +-------------------------------------------------------------------
 * Date: 2021/06/17 17:40:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.www.application.index.controller;

import org.quickjava.framework.module.Controller;

public class Verify extends Controller {

    public void index()
    {
        redirect("form");
    }

    public void form()
    {
        if (request.isPost()) {
            datum("message", "账号错误");
        }

        render();
    }

}
