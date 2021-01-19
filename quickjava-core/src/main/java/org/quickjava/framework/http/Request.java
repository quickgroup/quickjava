package org.quickjava.framework.http;

import org.quickjava.common.QUtils;
import org.quickjava.framework.App;
import org.quickjava.framework.config.AppConfig;
import org.quickjava.framework.controller.Action;
import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * 每个请求进来对应一个
 */
public class Request {

    protected String url;

    /**
     * @langCn url中的域名
     */
    protected String domin = null;

    /**
     * @langCn header头中的host，常规情况下和{@see #domin}一致，某些特殊场景会不同
     */
    protected String host = null;

    /**
     * @langCn 请求端口号
     */
    protected Integer port = null;

    /**
     * @langCn 请求类型，大写
     */
    protected String method = null;

    /**
     * eq pathinfo
     * @langCn 域名后面的路径，pathinfo格式{@code "/index/index/index"}
     */
    protected String path;

    /**
     * @langCn Pathinfo
     */
    protected Pathinfo pathinfo;

    /**
     * @langCn 客户端IP
     */
    protected String ip;

    /**
     * @langCn 开始时间
     */
    protected Long startTime;

    /**
     * @langCn 对应控制器
     */
    protected Controller controller = null;

    /**
     * @langCn 对应控制器的方法
     */
    protected Action action = null;

    /**
     * @langCn 请求header头
     */
    protected Dict headers = null;

    /**
     * @langCn 请求数据类型
     */
    protected Http.ContentType contentType = null;

    /**
     * @langCn url-query请求数据
     */
    protected Dict queryData = null;

    /**
     * @langCn Post请求数据
     */
    protected Dict postData = null;

    /**
     * @langCn 原始HttpServletRequest
     */
    protected HttpServletRequest httpServletRequest = null;

    /**
     * @langCn 原始HttpServletResponse
     */
    protected HttpServletResponse httpServletResponse = null;

    public Request(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.startTime = QUtils.getTimestamp();
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.initServlet();
        this.initHeader();
        this.initData();
    }

    /**
     * @langCn 对Servlet进行必要配置：编码、语言
     */
    protected void initServlet()
    {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader(Http.HeaderKey.Content_Type,"text/html; charset=UTF-8");
        httpServletResponse.setHeader(Http.HeaderKey.Server, App.name);
        httpServletResponse.setHeader(":"+Http.HeaderKey.Server, App.name);
    }

    @SuppressWarnings({"unchecked"})
    protected void initHeader()
    {
        this.url = httpServletRequest.getRequestURL().toString();
        this.pathinfo = Pathinfo.parseFromUrl(this.url);
        this.port = httpServletRequest.getLocalPort();
        this.path = httpServletRequest.getPathInfo();
        this.method = httpServletRequest.getMethod().toUpperCase();
        this.domin = this.pathinfo.hostname;
        this.ip = httpServletRequest.getRemoteAddr();
        // 解析默认模块/控制器/方法
        Dict config = AppConfig.config.get("module").get("default");
        this.pathinfo.parseAction(config.getString("module"), config.getString("controller"), config.getString("action"));
    }

    protected void initData()
    {
        this.postData = new Dict(httpServletRequest.getParameterMap());
        this.queryData = new Dict(this.pathinfo.queryData);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomin() {
        return domin;
    }

    public void setDomin(String domin) {
        this.domin = domin;
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

    public Pathinfo getPathinfo() {
        return pathinfo;
    }

    public void setPathinfo(Pathinfo pathinfo) {
        this.pathinfo = pathinfo;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    /**
     * @langCn 获取客户端(用户)IP
     * @return IP
     */
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
     * @param name
     * @return
     */
    public Dict getQueryData(String name)
    {
        return this.queryData;
    }

    public String getQuery(String name)
    {
        return this.queryData.getString(name);
    }

    /**
     * 获取[POST]的字符串数据，比如UrlEncode、Form-Data
     * 非字符串数据将报错 {@throw}，获取文件建议使用{@file(String name)}
     * @param name
     * @return
     */
    public Dict getPostData(String name)
    {
        return this.postData;
    }

    public String getPost(String name)
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
    protected Dict session = null;
}
