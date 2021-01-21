package org.quickjava.framework.exception;

import org.quickjava.common.QUtils;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;

public class QuickException extends RuntimeException {

    private QuickExceptionCode code = null;

    private Throwable targetThrowable = null;

    public QuickException(QuickExceptionCode code) {
        super(code.getMsg());
        this.code = code;
    }

    public QuickException(String message) {
        super(message);
        this.code = QuickExceptionCode.ERROR.setMsg(message);
    }

    public QuickException(Throwable targetThrowable) {
        this.code = QuickExceptionCode.ERROR.setMsg(targetThrowable.getMessage());
        this.targetThrowable = targetThrowable;
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
    public String output(Request request, Response response)
    {
        Throwable throwable = (targetThrowable == null) ? this : targetThrowable;
        System.out.println("targetThrowable: " + targetThrowable);

        StringBuffer output = new StringBuffer();
        output.append("<title>系统异常</title>");
        output.append("<h2>Exception: " + throwable.getClass().getTypeName() + "</h2>");
        output.append("<h3>Message: " + throwable.getMessage() + "</h3>");
        output.append("<h4>StackTrace:</h4><pre style=\"background: #eee;overflow: scroll;\">" + QUtils.stackTraceArrToString(throwable.getStackTrace()) + "</pre><br>");

        // 设置返回头
        response.setStatus(this.getCode().getStatus());

        return output.toString();
    }
}
