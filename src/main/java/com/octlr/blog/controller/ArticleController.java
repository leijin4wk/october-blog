package com.octlr.blog.controller;

import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.common.BaseResponse;
import com.octlr.blog.entity.Article;
import com.octlr.blog.service.ArticleService;
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
                                                                     @RequestParam(defaultValue = "10")Integer pageSize)
    {
        return BaseResponse.success(articleService.findArticleByPage(pageNum,pageSize));
    }

    @GetMapping("/search/{id}")
    public BaseResponse<Article> findArticleById(@PathVariable Integer id)
    {
        return BaseResponse.success(articleService.findArticleById(id));
    }
    @GetMapping("/category")
    public BaseResponse<BasePageResponse<Article>> findArticleByCategory(@RequestParam Integer categoryId,
                                                                         @RequestParam(defaultValue = "0") Integer pageNum,
                                                                         @RequestParam(defaultValue = "10")Integer pageSize)
    {
        return BaseResponse.success(articleService.findArticleByCategoryId(categoryId,pageNum,pageSize));
    }
    @GetMapping("/exist")
    public BaseResponse<Boolean> existArticle( @RequestParam String title, @RequestParam String description)
    {
        return BaseResponse.success(articleService.existArticle(title,description));
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
