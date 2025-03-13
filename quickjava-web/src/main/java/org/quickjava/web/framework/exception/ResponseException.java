package org.quickjava.web.framework.exception;

import org.quickjava.web.framework.response.QuickResponse;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:52
 */
public class ResponseException extends QuickException {

    private QuickResponse quickResponse;

    public ResponseException(QuickResponse quickResponse) {
        super(QuickExceptionCode.RESPONSE);
        this.quickResponse = quickResponse;
    }

    public ResponseException(QuickExceptionCode code, QuickResponse quickResponse) {
        super(code);
        this.quickResponse = quickResponse;
    }

    public QuickResponse getQuickResponse() {
        return quickResponse;
    }

    public void setQuickResponse(QuickResponse quickResponse) {
        this.quickResponse = quickResponse;
    }

}
