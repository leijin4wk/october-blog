package com.octlr.blog.controller;

import com.octlr.blog.common.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {
    @GetMapping("/")
    public BaseResponse<String> findAll()
    {
        return BaseResponse.success("项目已经启动！");
    }
}
