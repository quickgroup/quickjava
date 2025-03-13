/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Controller.java
 * +-------------------------------------------------------------------
 * Date: 2021/11/17 15:45:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.framework.module;

import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.exception.ResponseException;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.module.path.Action;
import org.quickjava.framework.module.path.ControllerPath;
import org.quickjava.framework.module.path.Module;
import org.quickjava.framework.response.JsonResponse;
import org.quickjava.framework.response.ViewResponse;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class Controller {

    public Module module;

    public ControllerPath controllerPath;

    public Action action;

    public String name;

    public String path;

    public Request request;

    public Response response;

    public Map<String, Object> viewData = new LinkedHashMap<>();

    public Controller() {
        this.name = this.getClass().getSimpleName();
    }

    public void _initialize(Action action)
    {
        this.module = action.controller.module;
        this.controllerPath = action.controller;
        this.action = action;
        this.path = module.getPath() + "/" + name;
    }

    public void _initRequest(Request request, Response response)
    {
        this.request = request;
        this.response = response;
    }

    /**
     * 重定向
     * @param url 目标地址可以是：url、控制器名称、控制器完整路径
     * @param statusCode HTTP状态码
     * @return
     */
    public Object redirect(String url, Integer statusCode) {
        return url;
    }

    public Object redirect(String url) {
        return redirect(url, 302);
    }

    /**
     * 返回结果
     * @return
     */
    public void result(int code, String msg, Object data)
    {
        Dict result = new Dict();
        result.set("code", code);
        result.set("msg", msg);
        result.set("data", data);
        throw new ResponseException(new JsonResponse(result));
    }

    public Object render()
    {
        String viewPath = this.name.toLowerCase() + "/" + action.name;
        return render(viewPath);
    }

    /**
     * #quickLang 渲染页面
     * @param targetPath 路径
     * @return
     */
    public Object render(String targetPath)
    {
        viewData.put("Request", request);

        String viewPath = this.module.viewPath + "/" + targetPath + ".html";
        ViewResponse viewResponse = new ViewResponse(new File(viewPath), viewData);
        throw new ResponseException(viewResponse);
    }

    /**
     * #quickLang 向view中添加数据
     * @param key
     * @param val
     */
    public void datum(String key, Object val)
    {
        viewData.put(key, val);
    }

    @Override
    public String toString() {
        return "Controller{" +
                ", name='" + name + '\'' +
                ", action=" + action +
                ", path='" + path + '\'' +
                ", request=" + request +
                ", response=" + response +
                '}';
    }
}
