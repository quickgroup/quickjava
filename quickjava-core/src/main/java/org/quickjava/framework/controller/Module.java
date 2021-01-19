package org.quickjava.framework.controller;

import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.config.AppConfig;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 20:56
 * @ProjectName quickjava
 */
public class Module {

    private String name;

    private String packages;

    public Module(String name, String packages) {
        this.name = name;
        this.packages = packages;
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
                '}';
    }
}
