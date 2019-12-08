package com.octlr.blog.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.octlr.blog.dto.OssDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Properties;
@Slf4j
public class OssUtils {
    public static OssDto readProperty(String configName) {
        File file = new File(configName);
        try {
            Properties props = new Properties();
            String str = FileUtils.readFileToString(file, "utf-8");
            props.load(new StringReader(str));
            OssDto ossDto= OssDto.builder().endpoint(props.getProperty("endpoint"))
                    .accessKeyId(props.getProperty("accessKeyId"))
                    .accessKeySecret(props.getProperty("accessKeySecret"))
                    .bucketName(props.getProperty("bucketName")).build();
            log.info(ossDto.toString());
            return ossDto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void uploadFile(OssDto ossDto,File file,String uploadName){
        InputStream inputStream = null;
        try {
            OSS ossClient = new OSSClientBuilder().build(ossDto.getEndpoint(),
                    ossDto.getAccessKeyId(),
                    ossDto.getAccessKeySecret());
            inputStream = new FileInputStream(file);
            PutObjectResult putObjectResult= ossClient.putObject(ossDto.getBucketName(),uploadName, inputStream);
            ossClient.shutdown();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
