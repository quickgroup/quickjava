package org.quickjava.core.http;

import org.quickjava.common.QUtils;
import org.quickjava.core.App;
import org.quickjava.core.controller.Action;
import org.quickjava.core.bean.Dict;
import org.quickjava.core.controller.Module;
import org.quickjava.core.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * 每个请求进来对应一个
 */
public class Request {

    /**
     * @langCn url中的域名
     */
    private String domin = null;

    /**
     * @langCn header头中的host，常规情况下和{@see #domin}一致，某些特殊场景会不同
     */
    private String host = null;

    /**
     * @langCn 请求端口号
     */
    private Integer port = null;

    /**
     * @langCn 请求类型，大写
     */
    private String method = null;

    /**
     * eq pathinfo
     * @langCn 域名后面的路径，pathinfo格式{@code "/index/index/index"}
     */
    private String path = null;

    /**
     * @langCn 对应控制器
     */
    private Controller controller = null;

    /**
     * @langCn 对应控制器的方法
     */
    private Action action = null;

    /**
     * @langCn 请求header头
     */
    private Dict headers = null;

    /**
     * @langCn 请求数据类型
     */
    private Http.ContentType contentType = null;

    /**
     * @langCn url-query请求数据
     */
    private Dict queryData = null;

    /**
     * @langCn Post请求数据
     */
    private Dict postData = null;

    /**
     * @langCn 原始HttpServletRequest
     */
    private HttpServletRequest httpServletRequest = null;

    /**
     * @langCn 原始HttpServletResponse
     */
    private HttpServletResponse httpServletResponse = null;

    public Request(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.initServlet();
        this.initHeader();
    }

    /**
     * @langCn 对Servlet进行必要配置：编码、语言
     */
    private void initServlet()
    {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader(Http.HeaderKey.Content_Type,"text/html; charset=UTF-8");
        httpServletResponse.setHeader(Http.HeaderKey.Server, App.name);
        httpServletResponse.setHeader(":"+Http.HeaderKey.Server, App.name);
    }

    @SuppressWarnings({"unchecked"})
    private void initHeader()
    {
        this.host = httpServletRequest.getRemoteHost();
        this.port = httpServletRequest.getRemotePort();
        this.path = httpServletRequest.getPathInfo();
        this.method = httpServletRequest.getMethod().toUpperCase();
        // 初始化请求数据
        Map<String, String[]> data = httpServletRequest.getParameterMap();
        this.postData = new Dict( (Map) data );
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
    private Dict session = null;
}
