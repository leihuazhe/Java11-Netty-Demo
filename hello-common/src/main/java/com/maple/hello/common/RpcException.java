package com.maple.hello.common;

/**
 * desc: RpcException
 *
 * @author hz.lei
 * @since 2018年08月26日 下午10:16
 */
public class RpcException extends RuntimeException {

    private static final long serialVersionUID = -129682168859027730L;

    private String code;
    private String msg;

    public RpcException(String code, String msg) {
        super(code + ":" + msg);

        this.code = code;
        this.msg = msg;
    }

    public RpcException(String code, String msg, Throwable cause) {
        super(cause);

        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return code + ":" + msg;
    }

}
