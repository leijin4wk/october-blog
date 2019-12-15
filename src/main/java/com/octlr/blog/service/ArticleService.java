package com.octlr.blog.service;

import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.entity.Article;

public interface ArticleService {
    BasePageResponse<Article> findArticleByPage(Integer pageNum,Integer pageSize);

    BasePageResponse<Article> findArticleByCategoryId(Integer categoryId,Integer pageNum,Integer pageSize);

    Article findArticleById(Integer id);

    void  saveArticle(Article article);
}
