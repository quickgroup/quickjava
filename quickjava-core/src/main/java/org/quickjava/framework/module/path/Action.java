/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Action.java
 * +-------------------------------------------------------------------
 * Date: 2021/11/17 15:49:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.framework.module.path;

import java.lang.reflect.Method;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/18 23:31
 * @projectName quickjava
 */
public class Action extends BasePath {

    public ControllerPath controller;

    public Method method;

    public Action(BasePath controller, Method method)
    {
        this.type = Type.ACTION;
        this.controller = (ControllerPath) controller;
        this.method = method;
        this.name = method.getName();
        this.path = controller.path + "/" + method.getName();
    }

    @Override
    public String toString() {
        return "Action{" +
                ", name='" + name + '\'' +
                ", method=" + method +
                ", path='" + path + '\'' +
                '}';
    }
}
