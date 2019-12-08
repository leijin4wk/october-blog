package com.octlr.blog.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class OssConfig {
    @Value("${local.config.path}")
    private String localConfigPath;
    @Value("${local.db.path}")
    private String localDbPath;
    @Value("${local.article.dir}")
    private String localArticlePath;

    @Value("${remote.db.name}")
    private String remoteDbName;
    @Value("${remote.article.dir}")
    private String remoteArticleDir;

}
