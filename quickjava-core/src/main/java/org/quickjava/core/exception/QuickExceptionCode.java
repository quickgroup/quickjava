package org.quickjava.core.exception;

public enum  QuickExceptionCode {

    ERROR(0, "默认"),
    SUCCESS(1, "成功"),
    COMPLETE(200, "成功"),
    ;

    private Integer type = 0;

    private Integer code = 0;

    private String msg = null;

    QuickExceptionCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public QuickExceptionCode setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "QuickExceptionCode{" +
                "type=" + type +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
