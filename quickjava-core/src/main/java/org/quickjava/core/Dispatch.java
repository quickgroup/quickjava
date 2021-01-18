package org.quickjava.core;

import org.quickjava.core.controller.Controller;
import org.quickjava.core.exception.QuickException;
import org.quickjava.core.http.Request;

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
        try {

            Request request = new Request(httpServletRequest, httpServletResponse);

            DispatchMethod dispatchMethod = findMappingMethod(request);

            methodInvok(dispatchMethod);

            httpServletResponse.setContentType("text/html");
            String name = httpServletRequest.getParameter("name");
            if (name == null) {
                name = "world";
            }
            PrintWriter pw = httpServletResponse.getWriter();
            pw.write("<h1>Hello, " + name + "!</h1>");
            pw.write("path: " + request.getPath());
            pw.flush();
        } catch (QuickException exc) {
            exc.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     * 找到控制器方法
     * @param request
     */
    private DispatchMethod findMappingMethod(Request request)
    {
        DispatchMethod dispatchMethod = new DispatchMethod();
//        dispatchMethod.controller = new Index();    // request 赋值
        return dispatchMethod;
    }

    private void methodInvok(DispatchMethod dispatchMethod)
    {

    }

    /**
     * 对应操作方法
     */
    public class DispatchMethod {

        public Request request;

        public Controller controller;

        public java.lang.reflect.Method targetMethod;
    }

    private void test(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
    {

    }
}
