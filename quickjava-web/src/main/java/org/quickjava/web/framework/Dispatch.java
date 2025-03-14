package org.quickjava.web.framework;

import org.quickjava.web.framework.bean.Dict;
import org.quickjava.web.framework.exception.QuickExceptionHandler;
import org.quickjava.web.framework.exception.ResponseException;
import org.quickjava.web.framework.http.Request;
import org.quickjava.web.framework.http.Response;
import org.quickjava.web.framework.response.JsonResponse;
import org.quickjava.web.framework.response.QuickResponse;
import org.quickjava.web.framework.response.TextResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * #quickLang 请求入口处理器
 * @author QloPC-Msi
 * #date 2021/01/21
 */
public class Dispatch {
    private static final Logger logger = LoggerFactory.getLogger(Dispatch.class);

    private static final Dispatch dispatch = new Dispatch();

    public static Dispatch get() {
        return dispatch;
    }

    public static void init()
    {
        logger.info(Lang.to("Dispatch init complete."));
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

        logger.info(request.method + " " + request.path);

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
            logger.debug(Lang.to("Processing time: ") + time + "ms");
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
