package org.quickjava.framework.module;

import java.lang.reflect.Method;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/18 23:31
 * @projectName quickjava
 */
public class Action {

    public Controller controller;

    public String name;

    public Method method;

    public String path;

    public Action(Controller controller, Method method) {
        this.controller = controller;
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
