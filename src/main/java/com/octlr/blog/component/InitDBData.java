package com.octlr.blog.component;

import com.octlr.blog.config.OssConfig;
import com.octlr.blog.dto.OssDto;
import com.octlr.blog.entity.Article;
import com.octlr.blog.entity.Classify;
import com.octlr.blog.repository.ArticleRepository;
import com.octlr.blog.repository.ClassifyRepository;
import com.octlr.blog.utils.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class InitDBData implements ApplicationRunner {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ClassifyRepository classifyRepository;
    @Autowired
    private OssConfig ossConfig;
    @Override
    public void run(ApplicationArguments args) {
        OssDto ossDto= OssUtils.readProperty(ossConfig.getConfigPath());
        Classify classify = Classify.builder()
                .build();
        classifyRepository.save(classify);
        Article article = Article.builder()
                .classifyId(classify.getId())
                .build();
        articleRepository.save(article);
        OssUtils.uploadFile(ossDto,ossConfig.getDbPath(),ossConfig.getDbName());
        log.info("初始化数据库！");
    }
}
