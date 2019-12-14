package com.octlr.blog;

import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.entity.Article;
import com.octlr.blog.repository.ArticleRepository;
import com.octlr.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
@Slf4j
class BlogApplicationTests {
    @Autowired
    private ArticleService articleService;
    @Test
    void findByPage() {
        BasePageResponse<Article> pageResponse= articleService.findArticleByPage("t",0,10);
        System.out.println(pageResponse);
    }
    @Test
    void findById() {
        Article article= articleService.findArticleById(2);
        System.out.println(article);
    }

}
