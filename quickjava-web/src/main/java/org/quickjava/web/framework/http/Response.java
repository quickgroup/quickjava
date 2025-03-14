package org.quickjava.web.framework.http;

import javax.servlet.http.HttpServletResponse;

/**
 * @author QloPC-zs
 * #date 2021/01/19
 */
public class Response {

    private Integer status = 200;

    private String contentType = null;

    private HttpServletResponse httpServletResponse;

    public Response(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
