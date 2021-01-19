package org.quickjava.framework.controller;

import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.exception.ResponseException;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.response.JsonResponse;

import java.util.Map;

public class Controller {

    protected Module module;

    protected String name;

    protected String packages;

    protected Request request;

    protected Response response;

    public Controller() {
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = (name.indexOf('.') == -1) ? name : name.substring(0, name.indexOf('.'));   // 存在.class
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getPath() {
        return module.getPath() + "/" + name;
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

    @Override
    public String toString() {
        return "Controller{" +
                "request=" + request +
                ", name='" + name + '\'' +
                ", packages='" + packages + '\'' +
                '}';
    }
}
