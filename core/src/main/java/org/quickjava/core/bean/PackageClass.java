package org.quickjava.core.bean;

public class PackageClass {

    private Type type = Type.Config;

    public enum Type {
        Config, // 配置文件
        Folder, // 文件夹
        Class,
    }
}
