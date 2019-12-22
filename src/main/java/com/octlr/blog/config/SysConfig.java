package com.octlr.blog.config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

@Component
@Slf4j
@Data
public class SysConfig implements InitializingBean {
    @Value("${local.config.path}")
    private String localConfigPath;
    @Value("${db.backup.bucketName}")
    private String dbBackName;
    private SysParams sysParams;
    @Override
    public void afterPropertiesSet() {
        log.info(localConfigPath);
        Properties props = readProperty(localConfigPath);
        if (props==null){
            log.error("加载自定义配置失败！");
            throw new RuntimeException("加载自定义配置失败！");
        }
        SysParams sysParams=new SysParams();
        sysParams.setEndpoint(props.getProperty("endpoint"));
        sysParams.setAccessKeyId(props.getProperty("accessKeyId"));
        sysParams.setAccessKeySecret(props.getProperty("accessKeySecret"));
        sysParams.setAuthToken(props.getProperty("authToken"));
        sysParams.setPhone(props.getProperty("phone"));
        sysParams.setPassword(props.getProperty("password"));
        setSysParams(sysParams);
        log.info(sysParams.toString());
    }
    @Data
    public class SysParams {
        private String endpoint;
        private String accessKeyId;
        private String accessKeySecret;
        private String authToken;
        private String phone;
        private String password;
    }
    private Properties readProperty(String configName) {
        File file = new File(configName);
        try {
            Properties props = new Properties();
            String str = FileUtils.readFileToString(file, "utf-8");
            props.load(new StringReader(str));
            return props;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
