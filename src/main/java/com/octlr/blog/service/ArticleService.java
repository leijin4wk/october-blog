package com.octlr.blog.service;

import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.entity.Article;

public interface ArticleService {
    BasePageResponse<Article> findArticleByPage(String search,Integer pageNum,Integer pageSize);

    Article findArticleById(Integer id);

    void  saveArticle(Article article);
}
