package org.demo.www.application.index.controller;

import org.quickjava.framework.module.Controller;

public class View extends Controller {

    public void index()
    {
        assign("msg", "来自View控制器的信息");
        assign("path", "你的访问路径是：" + request.path);
        assign("a", request.queryData.getString("a", ""));
        System.out.println("dict: " + request.queryData);
        view();
    }

}
