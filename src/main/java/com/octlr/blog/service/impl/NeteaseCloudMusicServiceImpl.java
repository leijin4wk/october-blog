package com.octlr.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.octlr.blog.config.SysConfig;
import com.octlr.blog.dto.NeteaseCloudMusicUserDto;
import com.octlr.blog.service.NeteaseCloudMusicService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class NeteaseCloudMusicServiceImpl implements NeteaseCloudMusicService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SysConfig sysConfig;
    @Value("${music.base.url}")
    private String NeteaseCloudMusicUrl;

    @Override
    public NeteaseCloudMusicUserDto getNeteaseCloudMusicCooike(){
        String value=redisTemplate.opsForValue().get("octlr:NeteaseCloudMusic:userDto");
        if (StringUtils.isEmpty(value)) {
            log.info("new request！");
            String uri = NeteaseCloudMusicUrl + "/login/cellphone?phone=" + sysConfig.getSysParams().getPhone() + "&password=" + sysConfig.getSysParams().getPassword();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            NeteaseCloudMusicUserDto neteaseCloudMusicUserDto=new NeteaseCloudMusicUserDto();
            Long accountId=JSON.parseObject(responseEntity.getBody().toString()).getJSONObject("account").getLong("id");
            neteaseCloudMusicUserDto.setAccountId(accountId);
            log.info(responseEntity.toString());
            List<String> cookies=responseEntity.getHeaders().get("set-cookie");
            neteaseCloudMusicUserDto.setCookie(cookies);
            redisTemplate.opsForValue().set("octlr:NeteaseCloudMusic:userDto", JSON.toJSONString(neteaseCloudMusicUserDto), 2, TimeUnit.HOURS);
            return neteaseCloudMusicUserDto;
        }else{
            return JSON.parseObject(value,NeteaseCloudMusicUserDto.class);
        }

    }

    @Override
    public void clearCookie() {
        redisTemplate.delete("octlr:NeteaseCloudMusic:userDto");
    }
    @Data
    class NeteaseCloudMusicBody{
        private String params;
        private String encSecKey;
    }
    private HttpEntity<NeteaseCloudMusicBody> buildHttpEntity(String data, Map<String,String> cookie){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
        headers.add("Referer","https://music.163.com");
        List<String> cookies =new ArrayList<>();
        cookies.add("");
        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<NeteaseCloudMusicBody> entity = new HttpEntity<>(headers);
        return entity;
    }
}
