package org.quickjava.framework.exception;

import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.response.QuickResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:52
 */
public class ResponseException extends QuickException {

    private QuickResponse quickResponse;

    public ResponseException(QuickResponse quickResponse) {
        super(QuickExceptionCode.RESPONSE);
        this.quickResponse = quickResponse;
    }

    public ResponseException(QuickExceptionCode code, QuickResponse quickResponse) {
        super(code);
        this.quickResponse = quickResponse;
    }

    public QuickResponse getQuickResponse() {
        return quickResponse;
    }

    public void setQuickResponse(QuickResponse quickResponse) {
        this.quickResponse = quickResponse;
    }

    /**
     * @langCn 异常响应处理
     */
    public static void onHandler(ResponseException exc, Request request, Response response)
    throws Exception
    {
        String outputBody = exc.getQuickResponse().output(response);
        if (outputBody == null)
            return;
        HttpServletResponse httpServletResponse = response.getHttpServletResponse();
        OutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.write(outputBody.getBytes());
        outputStream.close();
    }
}
