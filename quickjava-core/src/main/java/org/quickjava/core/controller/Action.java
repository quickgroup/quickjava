package org.quickjava.core.controller;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/18 23:31
 * @projectName quickjava
 */
@Data
public class Action {

    private Controller controller;

    private Class controllerClass;

    private String name;

    private Method method;

    public Action(Controller controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.name = method.getName();
    }

    public String getPath() {
        return controller.getPath() + "/" + method.getName();
    }

    @Override
    public String toString() {
        return "Action{" +
                "controller=" + controller +
                ", name='" + name + '\'' +
                ", method=" + method +
                '}';
    }
}
