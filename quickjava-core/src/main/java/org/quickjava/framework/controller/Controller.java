package org.quickjava.framework.controller;

import org.quickjava.common.QLog;
import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.exception.ResponseException;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.response.JsonResponse;
import org.quickjava.framework.response.ViewResponse;
import org.quickjava.framework.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

public class Controller {

    public Module module;

    public String name;

    public Action action;

    public String path;

    public String viewPath;

    public String packages;

    public Request request;

    public Response response;

    public Map<String, Action> actionList = new LinkedHashMap<>();

    public Controller() {
        this.name = this.getClass().getSimpleName();
        this.packages = this.getClass().getPackage().getName();
    }

    public void _initialize()
    {
    }

    public void _initRequest(Request request, Response response)
    {
        this.request = request;
        this.response = response;
    }

    public void setModule(Module module) {
        this.module = module;
        this.path = module.getPath() + "/" + name;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getViewPath() {
        return this.viewPath;
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

    public Object view()
    {
        String viewPath = this.name.toLowerCase() + "/" + action.name;
        return view(viewPath);
    }

    public Object view(String targetPath)
    {
        String viewPath = this.viewPath + "/" + targetPath + ".html";
        System.out.println("viewPath: " + viewPath);
        ViewResponse viewResponse = new ViewResponse(new View(viewPath));
        return new ViewResponse(new View(""));
    }

    @Override
    public String toString() {
        return "Controller{" +
                ", name='" + name + '\'' +
                ", action=" + action +
                ", path='" + path + '\'' +
                ", viewPath='" + viewPath + '\'' +
                ", packages='" + packages + '\'' +
                ", request=" + request +
                ", response=" + response +
                ", actionList=" + actionList +
                '}';
    }
}
