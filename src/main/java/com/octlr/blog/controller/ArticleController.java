package com.octlr.blog.controller;

import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.common.BaseResponse;
import com.octlr.blog.entity.Article;
import com.octlr.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("")
    public BaseResponse<BasePageResponse<Article>> findArticleByPage(@RequestParam(defaultValue = "0") Integer pageNum,
                                                                     @RequestParam(defaultValue = "10")Integer pageSize)
    {
        return BaseResponse.success(articleService.findArticleByPage(pageNum,pageSize));
    }

    @GetMapping("/{id}")
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

    @PostMapping("/")
    public BaseResponse<String> addArticle(@RequestBody Article article)
    {
        articleService.saveArticle(article);
        return BaseResponse.success("success");
    }

    @PutMapping("/{id}")
    public BaseResponse<String> updateArticle(@PathVariable Integer id,@RequestBody Article article)
    {
        article.setId(id);
        articleService.saveArticle(article);
        return BaseResponse.success("success");
    }
}
