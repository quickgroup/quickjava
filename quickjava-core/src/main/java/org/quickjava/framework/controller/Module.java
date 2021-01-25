package org.quickjava.framework.controller;

import org.quickjava.common.QUtils;
import org.quickjava.framework.App;
import org.quickjava.framework.bean.Dict;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 20:56
 * @ProjectName quickjava
 */
public class Module {

    public String name;

    public String packages;

    /**
     * @langCn 文件路径
     */
    public String dirpath;

    /**
     * @langCn 路由路径(网址路径)
     */
    public String path;

    public String controllerPath;

    public String modelPath;

    public String viewPath;

    public Map<String, Controller> controllerList = new LinkedHashMap<>();

    public Module(String name, String packages, String dirpath) {
        this.name = name;
        this.packages = packages;
        this.dirpath = dirpath;
        this.path = "/" + name;

        Dict dirDict = App.config.get("module").get("dirname");
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

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getPath() {
        return "/" + this.name;
    }

    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                ", packages='" + packages + '\'' +
                ", dirpath='" + dirpath + '\'' +
                ", path='" + path + '\'' +
                ", controllerList=" + controllerList +
                '}';
    }
}
