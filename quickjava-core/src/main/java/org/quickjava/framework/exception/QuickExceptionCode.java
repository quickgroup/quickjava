package org.quickjava.framework.exception;

public enum  QuickExceptionCode {

    ERROR(0, 500, "失败"),
    SUCCESS(1, 200, "成功"),
    RESPONSE(2, 200, "成功"),
    ;

    private Integer code = 0;

    private Integer status = 200;   // http状态码

    private String msg = null;

    QuickExceptionCode(Integer code,Integer status, String msg) {
        this.code = code;
        this.status = status;
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

    public Integer getStatus() {
        return status;
    }

    public QuickExceptionCode setStatus(Integer status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "QuickExceptionCode{" +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
