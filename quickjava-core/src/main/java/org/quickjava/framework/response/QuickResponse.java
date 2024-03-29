package org.quickjava.framework.response;

import org.quickjava.common.QuickLog;
import org.quickjava.framework.http.Http;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:51
 */
public abstract class QuickResponse {

    /**
     * @langCn 返回内容
     */
    protected byte[] content = null;

    /**
     * @langCn 返回的内容格式
     */
    protected String contentType = "text/html; charset=UTF-8";

    /**
     * @langCn 输出渲染方法 {子类必须实现该方法}
     * @param response
     * @return
     */
    abstract public byte[] render(Request request, Response response);

    /**
     * @langCn 输出数据写入到输出流
     * @param request 请求对象
     * @param response 输出对象
     */
    public void outputWrite(Request request, Response response)
    {
        try {
            HttpServletResponse httpServletResponse = response.getHttpServletResponse();

            // 状态设置
            httpServletResponse.setStatus(response.getStatus());
            httpServletResponse.setHeader(Http.HeaderKey.Content_Type, contentType);
//            httpServletResponse.setHeader("#" + Http.HeaderKey.Server, App.name);

            // 输出内容
            OutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(content);
            outputStream.close();

        } catch (Exception exc) {
            exc.printStackTrace();
            QuickLog.fatal(exc);
        }
    }

    /**
     * @langCn 输出字符转码
     * @return @langCn 转码完成后的字节码
     */
    public static byte[] stringToBytes(String string)
    {
        try {
            return string.getBytes(Charset.forName("UTF-8"));   // @langCn 后面读取配置
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
