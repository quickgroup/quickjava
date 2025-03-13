package org.quickjava.framework;

import org.quickjava.common.QuickLog;
import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.exception.QuickExceptionHandler;
import org.quickjava.framework.exception.ResponseException;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.response.JsonResponse;
import org.quickjava.framework.response.QuickResponse;
import org.quickjava.framework.response.TextResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * #quickLang 请求入口处理器
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
        QuickLog.info(Lang.to("Dispatch init complete."));
    }

    /**
     * @kangCn 处理请求
     */
    public void exec(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        long startNanoTime = System.nanoTime();

        Request request = new Request(httpServletRequest, httpServletResponse);
        Response response = new Response(httpServletResponse);

        Kernel.setCurrentRequest(request);

        QuickLog.info(request.method + " " + request.path);

        try {

            Route.RequestAction requestAction = Route.get().findMappingAction(request);

            Object result = requestAction.call(request, response);

            if (result == null) return;

            if (QuickResponse.class.isAssignableFrom(result.getClass())) {
                throw new ResponseException( (QuickResponse) result);
            } else if (result instanceof Map) {
                throw new ResponseException( new JsonResponse( (Map) result ));
            } else if (result instanceof Dict) {
                throw new ResponseException( new JsonResponse( (Dict) result ));
            }

            throw new ResponseException(new TextResponse(result.toString()));

        } catch (Throwable throwable) {
            QuickExceptionHandler.onHandler(throwable, request, response);
        } finally {

            clearCurrentRequest(request, response);

            double time = ((double)(System.nanoTime() - startNanoTime)) / 1000000L;
            QuickLog.debug(Lang.to("Processing time: ") + time + "ms");
        }
    }

    /**
     * #quickLang 清理本连接环境
     */
    private void clearCurrentRequest(Request request, Response response)
    {
        Kernel.setCurrentRequest(null);
    }

}
