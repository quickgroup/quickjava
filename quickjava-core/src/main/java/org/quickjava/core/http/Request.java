package org.quickjava.core.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;

/**
 * 每个请求进来对应一个
 */
public class Request {

    private String host = null;

    private Integer port = 80;

    private HashMap<String, String> headers = new HashMap<String, String>();

    private String method = null;

    private String path = null;

    private String query = null;

    private HttpServletRequest httpServletRequest = null;

    private HttpServletResponse httpServletResponse = null;

    public Request(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.init(httpServletRequest, httpServletResponse);
    }

    private void init(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        this.host = httpServletRequest.getRemoteHost();
        this.path = httpServletRequest.getPathInfo();
        this.method = httpServletRequest.getMethod();
        // headers
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    /**
     * 获取[GET]数据
     * @param name
     * @return
     */
    public String get(String name)
    {
        return null;
    }

    /**
     * 获取[POST]的字符串数据，比如UrlEncode、Form-Data
     * 非字符串数据将报错 {@throw}，获取文件建议使用{@file(String name)}
     * @param name
     * @return
     */
    public String post(String name)
    {
        return null;
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
}
