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

        Dict dirnameConfig = AppConfig.config.get("module").get("dirname");
        this.controllerPath = this.dirpath + "/" + dirnameConfig.getStringOrDef("controller", "controller");
        this.modelPath = this.dirpath + "/" + dirnameConfig.getStringOrDef("model", "model");
        this.viewPath = this.dirpath + "/" + dirnameConfig.getStringOrDef("view", "view");
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
