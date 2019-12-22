package com.octlr.blog.controller;

import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.common.BaseResponse;
import com.octlr.blog.entity.Article;
import com.octlr.blog.service.ArticleService;
import com.octlr.blog.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/search")
    public BaseResponse<BasePageResponse<Article>> findArticleByPage(@RequestParam(defaultValue = "0") Integer pageNum,
                                                                     @RequestParam(defaultValue = "10")Integer pageSize,
                                                                     @RequestParam(required = false) Integer categoryId)
    {
        return BaseResponse.success(articleService.findArticleByPage(pageNum,pageSize,categoryId));
    }

    @GetMapping("/search/{id}")
    public BaseResponse<ArticleVo> findArticleById(@PathVariable Integer id)
    {
        return BaseResponse.success(articleService.findArticleById(id));
    }
    @PostMapping("/add")
    public BaseResponse<String> addArticle(@RequestBody Article article)
    {
        article.setCreateTime(new Date());
        articleService.saveArticle(article);
        return BaseResponse.success("success");
    }

    @PutMapping("/update/{id}")
    public BaseResponse<String> updateArticle(@PathVariable Integer id,@RequestBody Article article)
    {
        article.setId(id);
        article.setUpdateTime(new Date());
        articleService.saveArticle(article);
        return BaseResponse.success("success");
    }
}
