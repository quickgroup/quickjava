/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Controller.java
 * +-------------------------------------------------------------------
 * Date: 2021/11/17 16:49:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.web.framework.module.path;

import java.util.LinkedHashMap;
import java.util.Map;

public class ControllerPath extends BasePath {

    public Module module;

    public Class<?> target;

    public Map<String, Action> actionList = new LinkedHashMap<>();

    public ControllerPath(BasePath module, Class<?> clazz)
    {
        this.type = Type.CONTROLLER;
        this.name = clazz.getSimpleName();
        this.module = (Module) module;
        this.target = clazz;
        this.path = module.getPath() + "/" + name;
    }

    @Override
    public String toString() {
        return "Controller{" +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", target='" + target + '\'' +
                ", actionList=" + actionList +
                '}';
    }
}
