package com.octlr.blog.common;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private boolean success;
    private String msg;
    private T data;
    public static <T> BaseResponse<T> success(T data){
        BaseResponse<T> baseResponse=new BaseResponse<>();
        baseResponse.setSuccess(true);
        baseResponse.setMsg("ok");
        baseResponse.setData(data);
        return baseResponse;
    }
}
