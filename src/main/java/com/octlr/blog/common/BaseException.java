package com.octlr.blog.common;

public class BaseException extends RuntimeException {
    private String msg;
    public BaseException(CodeMsg codeMsg) {
        super(codeMsg.getName());
        this.msg = codeMsg.getName();
    }
    public BaseException(String message) {
        super(message);
        this.msg = message;
    }
    public String getMsg() {
        return msg;
    }
}
