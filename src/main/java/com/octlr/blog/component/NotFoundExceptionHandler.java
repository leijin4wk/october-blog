package com.octlr.blog.component;

import com.octlr.blog.common.BaseResponse;
import com.octlr.blog.common.CodeMsg;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotFoundExceptionHandler implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }
    @RequestMapping(value = {"/error"})
    @ResponseBody
    public Object error(HttpServletRequest request) {
        return BaseResponse.error(CodeMsg.NotFoundError);
    }
}
