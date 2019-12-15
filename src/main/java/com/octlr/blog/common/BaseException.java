package com.octlr.blog.common;

public class BaseException extends RuntimeException {
    private String msg;
    public BaseException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.msg = codeMsg.getName();
    }
    public BaseException(CodeMsg codeMsg,String message) {
        super(codeMsg.toString());
        this.msg = message;
    }

    public String getCodeMsg() {
        return msg;
    }
}
