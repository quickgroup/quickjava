package org.demo.www.application.index.controller;

import org.quickjava.core.controller.Controller;

public class Index extends Controller {

    /**
     * 首页
     * @return
     */
    public Object index()
    {
        return "Hello, world!";
    }

    /**
     * 测试方法
     * @return
     */
    public Object indexTest()
    {
        return "Hello, indexTest!";
    }

    private Object indexPrivate()
    {
        return "Hello, indexPrivate!";
    }

}
