/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Module.java
 * +-------------------------------------------------------------------
 * Date: 2021/11/17 15:49:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.web.framework.module.path;

import org.quickjava.web.framework.Kernel;
import org.quickjava.web.framework.bean.Dict;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-Msi)
 * #date 2021/1/17 20:56
 * @ProjectName quickjava
 */
public class Module extends BasePath {

    public String name;

    /**
     * #quickLang 文件路径
     */
    public String dirpath;

    /**
     * #quickLang 路由路径(网址路径)
     */
    public String path;

    public String controllerPath;

    public String modelPath;

    public String viewPath;

    public Map<String, ControllerPath> controllerList = new LinkedHashMap<>();

    public Module(String name, String dirPath)
    {
        this.type = Type.MODULE;
        this.name = name;
        this.dirpath = dirPath;
        this.path = "/" + name;

        Dict dirDict = Kernel.config().getDict("module").getDict("dirname");
        this.controllerPath = this.dirpath + "/" + dirDict.getString("controller", "controller");
        this.modelPath = this.dirpath + "/" + dirDict.getString("model", "model");
        this.viewPath = this.dirpath + "/" + dirDict.getString("view", "view");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return "/" + this.name;
    }

    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                ", dirpath='" + dirpath + '\'' +
                ", path='" + path + '\'' +
                ", controllerList=" + controllerList +
                '}';
    }
}
