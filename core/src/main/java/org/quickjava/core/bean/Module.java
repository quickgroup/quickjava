package org.quickjava.core.bean;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 20:56
 * @ProjectName quickjava
 */
public class Module {

    private String name = null;

    private String packagePath = null;

    public Module(String name, String packagePath) {
        this.name = name;
        this.packagePath = packagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                ", packagePath='" + packagePath + '\'' +
                '}';
    }
}
