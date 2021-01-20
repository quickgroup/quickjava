package org.quickjava.framework;

import org.quickjava.common.QLog;
import org.quickjava.common.QUtils;
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
 * 拦截器/分发器
 */
public class Dispatch {

    // singleton
    private static Dispatch dispatch = new Dispatch();

    /**
     * 用户定义的拦截器
     */
    private static List<Object> dispatchList = new ArrayList<Object>();

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
        Request request = null;
        Response response = null;
        try {
            request = new Request(httpServletRequest, httpServletResponse);
            response = new Response(httpServletResponse);

            try {
                Route.MapAction mapAction = Route.get().findMappingAction(request);

                Object result = mapAction.invoke(request, response);

                if (result == null) return;

                if (QuickResponse.class.isAssignableFrom(result.getClass())) {
                    throw new ResponseException( (QuickResponse) result);
                }
                throw new ResponseException(new QuickResponse(result.toString()));

            } catch (ResponseException exc) {
                ResponseException.onHandler(exc, request, response);
            } finally {
                Long time = QUtils.getTimestamp() - request.getStartTime();
                QLog.info("startTime: " + time + "ms");
            }

        } catch (Exception exc) {
            QuickExceptionHandler.onHandler(exc, httpServletRequest, httpServletResponse);
        }
    }

}
