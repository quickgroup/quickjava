package org.quickjava.core.controller;

import org.quickjava.core.http.Request;
import org.quickjava.core.http.Response;

public class Controller {

    private Module module;

    private String name;

    private String packages;

    private Request request;

    private Response response;

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

    @Override
    public String toString() {
        return "Controller{" +
                "request=" + request +
                ", name='" + name + '\'' +
                ", packages='" + packages + '\'' +
                '}';
    }
}
