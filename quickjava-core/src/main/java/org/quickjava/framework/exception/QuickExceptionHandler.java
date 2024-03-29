package org.quickjava.framework.exception;

import org.quickjava.common.QuickLog;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.response.QuickResponse;
import org.quickjava.framework.response.TextResponse;

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
            QuickResponse quickResponse = null;

            if (thr instanceof ResponseException) {
                ResponseException responseException = (ResponseException) thr;
                quickResponse = responseException.getQuickResponse();

            } else if (thr instanceof ActionNotFoundException) {
                ActionNotFoundException exception = (ActionNotFoundException) thr;
                quickResponse = new TextResponse(exception.output(request, response));
                QuickLog.error(exception.getMessage());

            } else {
                throw thr;
            }

            if (quickResponse == null)
                return;

            quickResponse.render(request, response);
            quickResponse.outputWrite(request, response);

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

        byte[] outputBytes = quickException.output(request, response);

        new TextResponse(outputBytes).outputWrite(request, response);
    }

}
