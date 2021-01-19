package org.quickjava.core;

import org.quickjava.core.exception.ActionNotFoundException;
import org.quickjava.core.exception.QuickException;
import org.quickjava.core.exception.QuickExceptionHandler;
import org.quickjava.core.exception.ResponseException;
import org.quickjava.core.http.Request;
import org.quickjava.core.http.Response;
import org.quickjava.core.response.QuickResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
     * 处理
     */
    public void exec(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException
    {
        Request request = new Request(httpServletRequest, httpServletResponse);
        Response response = new Response(httpServletResponse);
        try {
            try {
                Route.MapAction mapAction = Route.get().findMappingAction(request);

                Object result = mapAction.invoke(request, response);

                if (result instanceof String) {
                    throw new ResponseException(new QuickResponse((String) result));
                }

            } catch (ResponseException exc) {
                ResponseException.onHandler(exc, request, response);
            }
        } catch (Exception exc) {
            QuickExceptionHandler.onHandler(exc, request, response);
        }
    }

    private void test(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
    {

    }
}
