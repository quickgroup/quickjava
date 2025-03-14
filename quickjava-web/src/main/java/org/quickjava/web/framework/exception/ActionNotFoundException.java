package org.quickjava.web.framework.exception;

/**
 * @author Qlo1062-(QloPC-Msi)
 * #date 2021/1/17 20:26
 * @ProjectName quickjava
 */
public class ActionNotFoundException extends QuickException {

    public ActionNotFoundException(String msg) {
        super(QuickExceptionCode.ERROR.setStatus(404).setMsg(msg));
    }
}
