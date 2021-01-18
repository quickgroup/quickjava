package org.quickjava.core.exception;

public class QuickException extends RuntimeException {

    private QuickExceptionCode code = null;

    public QuickException() {
    }

    public QuickException(QuickExceptionCode code) {
        this.code = code;
    }

}
