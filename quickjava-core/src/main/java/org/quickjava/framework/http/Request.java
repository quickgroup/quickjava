package org.quickjava.framework.http;

import org.quickjava.common.QuickUtil;
import org.quickjava.framework.Kernel;
import org.quickjava.framework.module.path.Action;
import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.module.path.ControllerPath;
import org.quickjava.framework.module.path.Module;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 每个请求进来对应一个
 */
public class Request {

    public String url;

    /**
     * @langCn url中的域名
     */
    public String domin = null;

    /**
     * @langCn header头中的host，常规情况下和{@see #domin}一致，某些特殊场景会不同
     */
    public String host = null;

    /**
     * @langCn 请求端口号
     */
    public Integer port = null;

    /**
     * @langCn 请求类型，大写
     */
    public String method = null;

    /**
     * eq pathinfo
     * @langCn 域名后面的路径，pathinfo格式{@code "/index/index/index"}
     */
    public String path;

    /**
     * @langCn http协议
     */
    public String protocol;

    /**
     * @langCn Pathinfo
     */
    public Pathinfo pathinfo;

    /**
     * @langCn 客户端IP
     */
    public String ip;

    /**
     * @langCn 开始时间
     */
    public Long startTime;

    /**
     * @langCn 对应模块
     */
    public Module module;
    public String moduleName;

    /**
     * @langCn 对应控制器
     */
    public ControllerPath controllerPath;
    public String controllerName;

    /**
     * @langCn 对应控制器的方法
     */
    public Action action;
    public String actionName;

    /**
     * @langCn 请求header头
     */
    public Dict headers = null;

    /**
     * @langCn 请求数据类型
     */
    public Http.ContentType contentType = null;

    /**
     * @langCn url-query请求数据
     */
    public Dict queryData = null;

    /**
     * @langCn Post请求数据
     */
    public Dict postData = null;

    /**
     * @langCn Get和Post请求数据，POST会覆盖get中重名的数据
     */
    public Dict params = null;

    /**
     * @langCn 原始HttpServletRequest
     */
    public ServletContext servletContext = null;

    /**
     * @langCn 原始HttpServletRequest
     */
    public HttpServletRequest httpServletRequest = null;

    /**
     * @langCn 原始HttpServletResponse
     */
    public HttpServletResponse httpServletResponse = null;

    public Request(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.startTime = QuickUtil.getTimestamp();
        this.servletContext = httpServletRequest.getServletContext();
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.initServlet();
        this.initHeader();
        this.initData();
    }

    /**
     * @langCn 对Servlet进行必要配置：编码、语言
     */
    public void initServlet()
    {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader(Http.HeaderKey.Server, Kernel.name);
        httpServletResponse.setHeader("x-server", Kernel.name);
    }

    @SuppressWarnings({"unchecked"})
    public void initHeader()
    {
        this.url = httpServletRequest.getRequestURL().toString();
        this.port = httpServletRequest.getLocalPort();
        this.path = httpServletRequest.getPathInfo();
        this.method = httpServletRequest.getMethod().toUpperCase();
        this.ip = httpServletRequest.getRemoteAddr();
        this.protocol = httpServletRequest.getProtocol();

        // 补全url
        if (httpServletRequest.getQueryString() != null) {
            this.url += "?" + httpServletRequest.getQueryString();
            this.path += "?" + httpServletRequest.getQueryString();
        }

        // pathinfo
        this.pathinfo = Pathinfo.parseFromUrl(this.url);
        this.domin = this.pathinfo.hostname;

        // 解析默认模块/控制器/方法
        Dict defaultConfig = Kernel.config.get("module").get("path");
        this.pathinfo.parseControllerAction(
                defaultConfig.getString("module"),
                defaultConfig.getString("module"),
                defaultConfig.getString("action"));
        this.moduleName = this.pathinfo.module;
        this.controllerName = this.pathinfo.controller;
        this.actionName = this.pathinfo.action;
    }

    public void initData()
    {
        this.postData = new Dict(httpServletRequest.getParameterMap());
        this.queryData = new Dict(this.pathinfo.queryData);
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public Boolean isPost() {
        return method.equals("POST") ? true : false;
    }

    public Boolean isAjax() {
        return isXMLHttpRequest();
    }

    public Boolean isXMLHttpRequest() {
        return ("XMLHttpRequest".equals(getHeader("x-requested-with"))) ? true : false;
    }

    /**
     * 获取header
     * @param name
     * @return
     */
    public String getHeader(String name) {
        return httpServletRequest.getHeader(name);
    }

    /**
     * 获取header
     * @param name
     * @return
     */
    public String getHeaderOrDefault(String name, String defaultValue) {
        String result = httpServletRequest.getHeader(name);
        return QuickUtil.stringIsEmpty(result) ? defaultValue : result;
    }

    /**
     * 获取[Query]数据
     */
    public String query(String name)
    {
        return this.queryData.getString(name);
    }

    /**
     * 获取[POST]数据，比如UrlEncode、Form-Data
     * 非字符串数据将报错 {@throw}，获取文件建议使用{@file(String name)}
     */
    public String post(String name)
    {
        return this.postData.getString(name);
    }

    /**
     * 获取Form-Data中的文件
     * @param name
     * @return
     */
    public File file(String name)
    {
        return null;
    }

    /**
     * Session
     */
    public Dict session = null;


    public String getUrl() {
        return url;
    }

    public String getDomin() {
        return domin;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public Pathinfo getPathinfo() {
        return pathinfo;
    }

    public String getIp() {
        return ip;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Module getModule() {
        return module;
    }

    public String getModuleName() {
        return moduleName;
    }

    public ControllerPath getControllerPath() {
        return controllerPath;
    }

    public String getControllerName() {
        return controllerName;
    }

    public Action getAction() {
        return action;
    }

    public String getActionName() {
        return actionName;
    }

    public Dict getHeaders() {
        return headers;
    }

    public Http.ContentType getContentType() {
        return contentType;
    }

    public Dict getQueryData() {
        return queryData;
    }

    public Dict getPostData() {
        return postData;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public Dict getSession() {
        return session;
    }
}
