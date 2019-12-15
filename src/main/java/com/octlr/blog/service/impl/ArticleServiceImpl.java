package com.octlr.blog.service.impl;

import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.entity.Article;
import com.octlr.blog.repository.ArticleRepository;
import com.octlr.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public BasePageResponse<Article> findArticleByPage(Integer pageNum, Integer pageSize) {
        Page<Article> page= articleRepository.findByPage(PageRequest.of(pageNum,pageSize));
        return BasePageResponse.<Article>builder().pageNum(pageNum).pageSize(pageSize)
                .total(page.getTotalElements()).totalPages(page.getTotalPages())
                .content(page.getContent()).build();
    }

    @Override
    public BasePageResponse<Article> findArticleByCategoryId(Integer categoryId, Integer pageNum, Integer pageSize) {
        Page<Article> page= articleRepository.findByCategoryId(categoryId,PageRequest.of(pageNum,pageSize));
        return BasePageResponse.<Article>builder().pageNum(pageNum).pageSize(pageSize)
                .total(page.getTotalElements()).totalPages(page.getTotalPages())
                .content(page.getContent()).build();
    }

    @Override
    public Article findArticleById(Integer id) {
        return articleRepository.findById(id).orElseThrow( () -> new RuntimeException());
    }

    @Override
    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    @Override
    public Boolean existArticle(String title, String description) {
        Article article=articleRepository.findArticleByTitleAndDescription(title,description);
        return article!=null?true:false;
    }

}
