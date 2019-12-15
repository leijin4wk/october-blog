package com.octlr.blog.task;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.octlr.blog.config.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Configuration
@Slf4j
public class BackupDataTask {
    @Autowired
    private SysConfig sysConfig;
    @Value("${spring.datasource.url}")
    private String url;

    @Scheduled(cron = "0 0/5 * * * ?")
    private void configureTasks() {
        log.info("执行定时任务");
    }

    public void uploadDbFile(String uploadName) {
        String fileName = url.substring(url.lastIndexOf(":") + 1);
        InputStream inputStream = null;
        try {
            OSS ossClient = new OSSClientBuilder().build(sysConfig.getSysParams().getEndpoint(),
                    sysConfig.getSysParams().getAccessKeyId(),
                    sysConfig.getSysParams().getAccessKeySecret());
            inputStream = new FileInputStream(fileName);
            PutObjectResult putObjectResult = ossClient.putObject(sysConfig.getDbBackName(), uploadName, inputStream);
            ossClient.shutdown();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
