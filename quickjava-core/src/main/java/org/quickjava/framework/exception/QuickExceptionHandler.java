package org.quickjava.framework.exception;

import org.quickjava.framework.http.Http;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;

import javax.servlet.http.HttpServletRequest;
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
    public static void onHandler(Throwable exc, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        try {
            // @langCn 控制台打印异常
            if (exc instanceof ActionNotFoundException) {
            } else {
                exc.printStackTrace();
            }

            // @langCn 异常反馈
            httpServletResponse.setContentType(Http.ContentType.HTML);
            if (exc instanceof QuickException) {
                QuickException quickException = (QuickException) exc;
                httpServletResponse.setStatus(quickException.getCode().getStatus());
            } else {
                httpServletResponse.setStatus(500);
            }

            PrintWriter printWriter = httpServletResponse.getWriter();
            StringBuffer output = new StringBuffer();
            output.append("<h2>Exception: " + exc.getClass().getTypeName() + "</h2>");
            output.append("<h3>Message: " + exc.getMessage() + "</h3>");
            output.append("<h4>StackTrace:</h4><pre>" + String.join("\n", stackTraceArrToStringArr(exc.getStackTrace())) + "</pre><br>");
            printWriter.write(output.toString());
            printWriter.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @langCn 堆栈数组转string数组
     * @param stackTraceElements
     * @return
     */
    private static String[] stackTraceArrToStringArr(StackTraceElement[] stackTraceElements)
    {
        String[] strings = new String[stackTraceElements.length];
        for (int fi = 0; fi < stackTraceElements.length; fi++) {
            strings[fi] = stackTraceElements[fi].toString();
        }
        return strings;
    }

}
