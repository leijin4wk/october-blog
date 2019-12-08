package com.octlr.blog.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class OssConfig {
    @Value("${oss.config.path}")
    private String configPath;
    @Value("${oss.db.path}")
    private String dbPath;
    @Value("${oss.db.name}")
    private String dbName;
    @Value("${oss.md.dir}")
    private String mdPath;
}
