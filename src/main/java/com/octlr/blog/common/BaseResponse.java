package com.octlr.blog.common;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private boolean success;
    private String msg;
    private Integer code;
    private T data;
    public static <T> BaseResponse<T> success(T data){
        BaseResponse<T> baseResponse=new BaseResponse<>();
        baseResponse.setSuccess(true);
        baseResponse.setCode(CodeMsg.Success.getCode());
        baseResponse.setMsg(CodeMsg.Success.getName());
        baseResponse.setData(data);
        return baseResponse;
    }

    public static BaseResponse<String> error(CodeMsg data){
        BaseResponse<String> baseResponse=new BaseResponse<>();
        baseResponse.setSuccess(false);
        baseResponse.setCode(data.getCode());
        baseResponse.setMsg(data.getName());
        baseResponse.setData("");
        return baseResponse;
    }
    public static BaseResponse<String> error(String msg){
        BaseResponse<String> baseResponse=new BaseResponse<>();
        baseResponse.setSuccess(false);
        baseResponse.setCode(CodeMsg.BusinessError.getCode());
        baseResponse.setMsg(CodeMsg.BusinessError.getName());
        baseResponse.setData(msg);
        return baseResponse;
    }
}
