package com.octlr.blog;

import com.octlr.blog.config.OssConfig;
import com.octlr.blog.dto.OssDto;
import com.octlr.blog.utils.OssUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class BlogApplicationTests {
    @Autowired
    private OssConfig ossConfig;
    @Test
    void contextLoads() {
        OssDto ossDto = OssUtils.readProperty(ossConfig.getLocalConfigPath());
        File file = new File(ossConfig.getLocalArticlePath());

    }

}
