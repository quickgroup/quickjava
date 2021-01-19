package org.quickjava.framework.http;

import javax.servlet.http.HttpServletResponse;

/**
 * @author QloPC-zs
 * @date 2021/01/19
 */
public class Response {

    private Http.ContentType contentType = null;

    private HttpServletResponse httpServletResponse;

    public Response(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public Http.ContentType getContentType() {
        return contentType;
    }

    public void setContentType(Http.ContentType contentType) {
        this.contentType = contentType;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }
}
