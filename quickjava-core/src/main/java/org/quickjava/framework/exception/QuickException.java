package org.quickjava.framework.exception;

import org.quickjava.common.QuickUtil;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.response.QuickResponse;

public class QuickException extends RuntimeException {

    private QuickExceptionCode code = null;

    public QuickException(QuickExceptionCode code) {
        super(code.getMsg());
        this.code = code;
    }

    public QuickException(String message) {
        super(message);
        this.code = QuickExceptionCode.ERROR.setMsg(message);
    }

    public QuickException(String message, Throwable th) {
        super(message);
        this.code = QuickExceptionCode.ERROR.setMsg(message);
        this.initCause(th);
    }

    public QuickException(Throwable th) {
        this.code = QuickExceptionCode.ERROR.setMsg(th.getMessage());
        this.initCause(th);
    }

    public QuickExceptionCode getCode() {
        return code;
    }

    public void setCode(QuickExceptionCode code) {
        this.code = code;
    }

    /**
     * @langCn 异常输出为页面
     * @param request
     * @param response
     * @return
     */
    public byte[] output(Request request, Response response)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<title>系统异常</title>");
        stringBuffer.append("<body>");
        stringBuffer.append("<h2>Exception: " + this.getClass().getTypeName() + "</h2>");
        stringBuffer.append("<h3>Message: " + this.getMessage() + "</h3>");
        stringBuffer.append("<h4>StackTrace:</h4><pre style=\"background: #eee;overflow: scroll;\">" + QuickUtil.stackTraceArrToString(this.getStackTrace()) + "</pre><br>");
        stringBuffer.append("</body>");

        // 设置返回头
        response.setStatus(this.getCode().getStatus());

        return QuickResponse.stringToBytes(stringBuffer.toString());
    }


}
