package com.octlr.blog.component;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.octlr.blog.config.OssConfig;
import com.octlr.blog.dto.OssDto;
import com.octlr.blog.entity.Article;
import com.octlr.blog.entity.Classify;
import com.octlr.blog.repository.ArticleRepository;
import com.octlr.blog.repository.ClassifyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;


@Component
@Slf4j
public class InitDBData implements ApplicationRunner {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ClassifyRepository classifyRepository;
    @Autowired
    private OssConfig ossConfig;
    private OssDto  ossDto;
    @Override
    public void run(ApplicationArguments args) {
        readProperty(ossConfig.getConfigPath());
        Classify classify = Classify.builder()
                .build();
        classifyRepository.save(classify);
        Article article = Article.builder()
                .classifyId(classify.getId())
                .build();
        articleRepository.save(article);
        uploadFile(ossConfig.getDbPath(),ossConfig.getDbName());
        log.info("初始化数据库！");
    }
    private void readProperty(String configName) {
        File file = new File(configName);
        try {
            String str = FileUtils.readFileToString(file, "utf-8");
            Properties props = new Properties();
            props.load(new StringReader(str));
            ossDto= OssDto.builder().endpoint(props.getProperty("endpoint"))
                    .accessKeyId(props.getProperty("accessKeyId"))
                    .accessKeySecret(props.getProperty("accessKeySecret"))
                    .bucketName(props.getProperty("bucketName")).build();
            log.info(ossDto.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadFile(String uploadFile,String uploadName){
        OSS ossClient = new OSSClientBuilder().build(ossDto.getEndpoint(),
                ossDto.getAccessKeyId(),
                ossDto.getAccessKeySecret());
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(uploadFile);
            PutObjectResult putObjectResult= ossClient.putObject(ossDto.getBucketName(),uploadName, inputStream);
            ossClient.shutdown();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
