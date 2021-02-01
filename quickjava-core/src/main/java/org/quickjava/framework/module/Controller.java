package org.quickjava.framework.module;

import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.exception.ResponseException;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.response.JsonResponse;
import org.quickjava.framework.response.ViewResponse;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class Controller {

    public Module module;

    public String name;

    public Action action;

    public String path;

    public Map<String, Object> viewData = new LinkedHashMap<>();

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
        this.module = request.module;
        this.action = request.action;
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
        viewData.put("Request", request);

        String viewPath = this.module.viewPath + "/" + targetPath + ".html";
        ViewResponse viewResponse = new ViewResponse(new File(viewPath), viewData);
        throw new ResponseException(viewResponse);
    }

    /**
     * @langCn 向view中添加变量数据
     * @param name
     * @param object
     */
    public void assign(String name, Object object)
    {
        viewData.put(name, object);
    }

    @Override
    public String toString() {
        return "Controller{" +
                ", name='" + name + '\'' +
                ", action=" + action +
                ", path='" + path + '\'' +
                ", packages='" + packages + '\'' +
                ", request=" + request +
                ", response=" + response +
                ", actionList=" + actionList +
                '}';
    }
}
