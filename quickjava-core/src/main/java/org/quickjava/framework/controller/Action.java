package org.quickjava.framework.controller;

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

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Class getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class controllerClass) {
        this.controllerClass = controllerClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
