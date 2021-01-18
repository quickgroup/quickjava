package org.quickjava.core.exception;

import org.quickjava.core.response.QuickResponse;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:52
 */
public class ResponseException extends QuickException {

    private QuickResponse quickResponse = null;

    public static ResponseException newInstance() {
        QuickExceptionCode code = QuickExceptionCode.SUCCESS;
        code.setType(1);
        return new ResponseException(code);
    }

    public ResponseException(QuickExceptionCode code) {
        super(code);
    }
}
