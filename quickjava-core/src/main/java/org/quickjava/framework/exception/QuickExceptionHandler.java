package org.quickjava.framework.exception;

import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.response.QuickResponse;

/**
 * @author QloPC-zs
 * @date 2021/1/19
 */
public class QuickExceptionHandler {


    /**
     * @langCn 异常响应处理输出
     */
    public static void onHandler(Throwable thr, Request request, Response response)
    {
        try {
            String outputBody = null;

            if (thr instanceof ResponseException) {
                ResponseException responseException = (ResponseException) thr;
                outputBody = responseException.getQuickResponse().output(request, response);
            } else {
                throw thr;
            }

            if (outputBody == null)
                return;
            QuickResponse.outputWrite(outputBody.getBytes(), request, response);

        } catch (Throwable throwable) {
            onHandlerTerminal(throwable, request, response);
        }
    }

    /**
     * @langCn 异常响应处理输出-最终级别
     */
    public static void onHandlerTerminal(Throwable thr, Request request, Response response)
    {
        thr.printStackTrace();

        QuickException quickException = new QuickException(thr);
        String outputBody = quickException.output(request, response);
        QuickResponse.outputWrite(outputBody.getBytes(), request, response);
    }

}
