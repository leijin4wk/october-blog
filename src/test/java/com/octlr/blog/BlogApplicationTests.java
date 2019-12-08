package com.octlr.blog;

import com.octlr.blog.component.InitWebAndData;
import com.octlr.blog.config.OssConfig;
import com.octlr.blog.dto.OssDto;
import com.octlr.blog.utils.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Iterator;

@SpringBootTest
@Slf4j
class BlogApplicationTests {
    @Autowired
    private InitWebAndData initDBData;
    @Test
    void uploadStatic() {
        initDBData.uploadStatic();
    }
    @Test
    public void uploadDataBase() {
        initDBData.uploadDataBase();
    }

}
