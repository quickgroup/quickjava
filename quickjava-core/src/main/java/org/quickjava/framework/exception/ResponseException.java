package org.quickjava.framework.exception;

import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.response.QuickResponse;

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
