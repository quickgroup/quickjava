package org.demo.www.application.index.controller;

import org.quickjava.framework.controller.Controller;

public class View extends Controller {

    public void index()
    {
        this.assign("msg", "来自View.class信息");
        this.assign("path", "你的访问路径是：" + request.path);
        this.assign("module", request.moduleName);
        this.assign("controller", request.controllerName);
        this.assign("action", request.actionName);
        this.assign("request", request);
        this.view();
    }

}
