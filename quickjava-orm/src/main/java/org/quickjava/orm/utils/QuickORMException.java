package org.quickjava.orm.utils;

public class QuickORMException extends RuntimeException{

    public QuickORMException(String message) {
        super(message);
    }

    public QuickORMException(Throwable cause) {
        super(cause);
    }
}
