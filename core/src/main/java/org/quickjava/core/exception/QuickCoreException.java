package org.quickjava.core.exception;

public class QuickCoreException extends RuntimeException {

    private Integer code = 0;

    private String msg = null;

    public QuickCoreException() {
    }

    public QuickCoreException(String msg) {
        this.msg = msg;
    }

    public QuickCoreException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public QuickCoreException(String message, Integer code, String msg) {
        super(message);
        this.code = code;
        this.msg = msg;
    }

    public QuickCoreException(String message, Throwable cause, Integer code, String msg) {
        super(message, cause);
        this.code = code;
        this.msg = msg;
    }

    public QuickCoreException(Throwable cause, Integer code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public QuickCoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code, String msg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.msg = msg;
    }
}
