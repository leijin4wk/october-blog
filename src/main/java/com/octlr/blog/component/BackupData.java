package com.octlr.blog.component;

import com.octlr.blog.config.OssConfig;
import com.octlr.blog.dto.OssDto;
import com.octlr.blog.utils.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;



@Component
@Slf4j
public class BackupData {
    @Autowired
    private OssConfig ossConfig;

    public void backupDataBase() {
        OssDto ossDto = OssUtils.readProperty(ossConfig.getLocalConfigPath());
        OssUtils.uploadFile(ossDto, new File(ossConfig.getLocalDbPath()), ossConfig.getRemoteDbName());
        log.info("数据库发布成功！");
    }
}
