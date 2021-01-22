package org.quickjava.framework;

import org.quickjava.common.QLog;
import org.quickjava.common.QUtils;
import org.quickjava.framework.exception.QuickException;
import org.quickjava.framework.exception.QuickExceptionHandler;
import org.quickjava.framework.exception.ResponseException;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.response.QuickResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @langCn 请求入口处理器
 * @author QloPC-Msi
 * @date 2021/01/21
 */
public class Dispatch {

    private static Dispatch dispatch = new Dispatch();

    public static Dispatch get() {
        return dispatch;
    }

    public static void init()
    {

    }

    /**
     * @kangCn 处理请求
     */
    public void exec(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        Request request = new Request(httpServletRequest, httpServletResponse);
        Response response = new Response(httpServletResponse);
        App.setCurrentRequest(request);

        QLog.info(request.method + " " + request.path + " " + request.protocol);

        try {
            Route.MapAction mapAction = Route.get().findMappingAction(request);

            Object result = mapAction.invoke(request, response);

            if (result == null) return;

            if (QuickResponse.class.isAssignableFrom(result.getClass())) {
                throw new ResponseException( (QuickResponse) result);
            }
            throw new ResponseException(new QuickResponse(result.toString()));

        } catch (Throwable throwable) {
            QuickExceptionHandler.onHandler(throwable, request, response);
        } finally {

            // Request环境清理
            App.setCurrentRequest(null);

            long time = QUtils.getTimestamp() - request.startTime;
            QLog.debug("Handling time: " + time + "ms");
        }
    }

}
