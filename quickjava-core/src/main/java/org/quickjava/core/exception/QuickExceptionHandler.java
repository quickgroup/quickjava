package org.quickjava.core.exception;

import org.quickjava.core.http.Http;
import org.quickjava.core.http.Request;
import org.quickjava.core.http.Response;
import org.quickjava.core.response.QuickResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * @author QloPC-zs
 * @date 2021/1/19
 */
public class QuickExceptionHandler {

    /**
     * @langCn 异常处理
     */
    public static void onHandler(Throwable exc, Request request, Response response)
    {
        try {
            // @langCn 控制台打印异常
            if (exc instanceof ActionNotFoundException) {
            } else {
                exc.printStackTrace();
            }

            // @langCn 异常反馈
            HttpServletResponse httpServletResponse = request.getHttpServletResponse();
            httpServletResponse.setContentType(Http.ContentType.HTML);
            if (exc instanceof QuickException) {
                QuickException quickException = (QuickException) exc;
                httpServletResponse.setStatus(quickException.getCode().getStatus());
            } else {
                httpServletResponse.setStatus(500);
            }

            PrintWriter printWriter = request.getHttpServletResponse().getWriter();
            printWriter.write("Exception: " + exc.getMessage() + "<br>");
            printWriter.write("StackTrace: " + Arrays.toString(exc.getStackTrace()) + "<br>");
            printWriter.write("path: " + request.getPath() + "<br>");
            printWriter.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
