package org.quickjava.framework.controller;

import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.config.AppConfig;

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

    public String dirpath;

    public String path;

    public Map<String, Controller> controllerList = new LinkedHashMap<>();

    public Module(String name, String packages, String dirpath) {
        this.name = name;
        this.packages = packages;
        this.dirpath = dirpath;
        this.path = "/" + name;
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

    /**
     * @langCn 初始化模块
     */
    public void init()
    {
        String packagePath = this.packages;
        Dict dirDict = AppConfig.config.get("module").get("dir");
        String controllerPath = packagePath + "/" + dirDict.getString("controller");
        String modelPath = packagePath + "/" + dirDict.getString("model");
        String viewPath = packagePath + "/" + dirDict.getString("view");
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
