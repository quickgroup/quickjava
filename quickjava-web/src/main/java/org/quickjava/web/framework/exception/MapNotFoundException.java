package org.quickjava.web.framework.exception;

/**
 * @author Qlo1062-(QloPC-Msi)
 * #date 2021/1/17 20:26
 * @ProjectName quickjava
 */
public class MapNotFoundException extends QuickException {

    public MapNotFoundException(String msg) {
        super(QuickExceptionCode.ERROR.setMsg(msg));
    }
}
