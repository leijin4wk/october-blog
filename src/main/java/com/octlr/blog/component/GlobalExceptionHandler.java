package com.octlr.blog.component;

import com.octlr.blog.common.BaseException;
import com.octlr.blog.common.BaseResponse;
import com.octlr.blog.common.CodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理类
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public BaseResponse<String> exceptionHandler(Exception e){
       e.printStackTrace();
        if(e instanceof BaseException) {
            BaseException exception = (BaseException) e;
            return BaseResponse.error(exception.getMessage());
        }else if(e instanceof NoHandlerFoundException){
            return BaseResponse.error(CodeMsg.NotFoundError);
        }
        else {
            return BaseResponse.error(CodeMsg.ServerError);
        }
    }
}