package com.octlr.blog;

import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.entity.Article;
import com.octlr.blog.service.ArticleService;
import com.octlr.blog.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class BlogApplicationTests {
    @Autowired
    private ArticleService articleService;

    @Test
    void findByPage() {
        BasePageResponse<Article> pageResponse = articleService.findArticleByPage( 0, 10,null);
        System.out.println(pageResponse);
    }

    @Test
    void findById() {
        ArticleVo article = articleService.findArticleById(1);
        System.out.println(article);
    }

}
