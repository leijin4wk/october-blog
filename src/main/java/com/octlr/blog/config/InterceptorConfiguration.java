package com.octlr.blog.config;

import com.octlr.blog.component.HttpInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class InterceptorConfiguration implements WebMvcConfigurer {
    @Autowired
    private SysConfig sysConfig;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if(StringUtils.isEmpty(sysConfig.getSysParams().getAuthToken())){
            throw new RuntimeException("authToken 没有配置！");
        }
        log.info("authToken:{}",sysConfig.getSysParams().getAuthToken());
        registry.addInterceptor(new HttpInterceptor(sysConfig.getSysParams().getAuthToken())).addPathPatterns("/**");
    }
}
