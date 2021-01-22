package org.quickjava.framework.http;

import org.quickjava.common.QLog;
import org.quickjava.common.QUtils;
import org.quickjava.framework.App;
import org.quickjava.framework.config.AppConfig;
import org.quickjava.framework.controller.Action;
import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.controller.Controller;
import org.quickjava.framework.controller.Module;

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
    public Controller controller;
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
        this.startTime = QUtils.getTimestamp();
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
        httpServletResponse.setHeader(Http.HeaderKey.Content_Type,"text/html; charset=UTF-8");
        httpServletResponse.setHeader(Http.HeaderKey.Server, App.name);
        httpServletResponse.setHeader(":" + Http.HeaderKey.Server, App.name);
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
        Dict defaultConfig = App.config.get("module").get("default");
        this.pathinfo.parseControllerAction(
                defaultConfig.getString("module"),
                defaultConfig.getString("controller"),
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
        return QUtils.stringIsEmpty(result) ? defaultValue : result;
    }

    /**
     * 获取[Query]数据
     */
    public Dict query() {
        return this.queryData;
    }

    public String query(String name)
    {
        return this.queryData.getString(name);
    }

    /**
     * 获取[POST]数据，比如UrlEncode、Form-Data
     * 非字符串数据将报错 {@throw}，获取文件建议使用{@file(String name)}
     */
    public Dict post()
    {
        return this.postData;
    }

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
}
