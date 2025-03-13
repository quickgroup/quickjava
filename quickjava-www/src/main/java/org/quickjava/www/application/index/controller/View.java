package org.quickjava.www.application.index.controller;

import org.quickjava.web.framework.module.Controller;

public class View extends Controller {

    public void index()
    {
        datum("msg", "来自View控制器的信息");
        datum("path", "你的访问路径是：" + request.path);
        datum("a", request.queryData.getString("a", ""));
        System.out.println("dict: " + request.queryData);
        render();
    }

}
