package org.quickjava.framework.response;

import org.quickjava.common.QLog;
import org.quickjava.framework.http.Http;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:51
 */
public class QuickResponse {

    /**
     * @langCn 返回内容
     */
    protected Object data;

    public QuickResponse() {
    }

    /**
     * @langCn 文本
     * @param data
     */
    public QuickResponse(String data) {
        this.data = data;
    }

    /**
     * @langCn 输出
     * @param response
     * @return
     */
    public byte[] output(Request request, Response response)
    {
        return data.toString().getBytes();
    }

    /**
     * 输出数据转化
     * @param bytes
     * @param response
     */
    public static void outputWrite(byte[] bytes, Request request, Response response)
    {
        try {
            HttpServletResponse httpServletResponse = response.getHttpServletResponse();

            // 状态设置
            httpServletResponse.setStatus(response.getStatus());
            httpServletResponse.setHeader(Http.HeaderKey.Content_Type, response.getContentType());

            // 输出内容
            OutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(bytes);
            outputStream.close();

        } catch (Exception exc) {
            // 致命异常
            exc.printStackTrace();
            QLog.fatal(exc);
        }

        QLog.debug(request.path + " " + response.getStatus());
    }

    /**
     * @langCn 统一字符转码
     * @return
     */
    public static byte[] stringToBytes(String string)
    {
        try {
            return string.getBytes("UTF-8");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }
}
