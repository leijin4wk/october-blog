package com.octlr.blog.common;



public enum  CodeMsg {
    Success(0,"success"),
    BusinessError(-1,"业务异常！"),
    AuthError(-8,"权限异常！"),
    NotFoundError(-9,"资源不存在！"),
    ServerError(-10,"服务器内部异常！");
    private Integer code;

    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    CodeMsg(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
