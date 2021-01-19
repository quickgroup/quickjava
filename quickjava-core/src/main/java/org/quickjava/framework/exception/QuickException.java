package org.quickjava.framework.exception;

public class QuickException extends RuntimeException {

    private QuickExceptionCode code = null;

    public QuickException(QuickExceptionCode code) {
        super(code.getMsg());
        this.code = code;
    }

    public QuickExceptionCode getCode() {
        return code;
    }

    public void setCode(QuickExceptionCode code) {
        this.code = code;
    }
}
