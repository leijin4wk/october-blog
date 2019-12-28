package com.octlr.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.octlr.blog.config.NeteaseCloudConfig;
import com.octlr.blog.config.SysConfig;
import com.octlr.blog.dto.NeteaseCloudMusicUserDto;
import com.octlr.blog.service.NeteaseCloudMusicService;
import com.octlr.blog.util.EncryptHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public void loginByCellPhone() {
        String json="{" +
                "\"phone\":\"18875016131\"," +
                "\"password\":\"fb15f40d7db80ac09a91f051b7a30c31\"," +
                "\"rememberLogin\":\"true\"," +
                "\"csrf_token\":\"\""+
                "}";
        Map<String,String> cookie=new HashMap<>();
        cookie.put("os","pc");
        sendRequest(NeteaseCloudConfig.loginCellphone,1,json,cookie);
    }

    private  ResponseEntity<String> sendRequest(String url,int apiType, String data,Map<String,String> cookie){
        HttpEntity<String> entity;
        if (apiType==1) {
//            JSONObject jsonObject=JSON.parseObject(data);
//            jsonObject.put("csrf_token","");
            entity = buildHttpEntity(apiType,data,cookie);
        }else{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("method","POST");
            jsonObject.put("url",url);
            jsonObject.put("params",JSON.parseObject(data));
            entity = buildHttpEntity(apiType,jsonObject.toJSONString(),cookie);
        }
        ResponseEntity responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(responseEntity.getHeaders().toString());
        return responseEntity;
    }
    /**
     *
     * @param apiType 1.webapi 2.linux api
     * @param data
     * @param cookie
     * @return
     */
    private HttpEntity<String> buildHttpEntity(int apiType, String data, Map<String,String> cookie){
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept","*/*");
        headers.add("referer","https://music.163.com");
        headers.add("content-type","application/x-www-form-urlencoded");
        List<String> cookies =new ArrayList<>();
        for (String key:cookie.keySet()) {
            cookies.add(key+"="+cookie.get(key));
        }
        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<String> entity;
        if (apiType==1){
            headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
            entity = new HttpEntity<>(buildWebApiBody(data),headers);
        }else{
            headers.add("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
            entity = new HttpEntity<>(buildLinuxBody(data),headers);
        }
        return entity;
    }

    private String  buildWebApiBody(String json){
        System.out.println(NeteaseCloudConfig.presetKey);
        System.out.println(NeteaseCloudConfig.iv);
        System.out.println(json);
        StringBuilder result=new StringBuilder();
        String secretKey= "2nTVKmx9y3BZUyJW";
        StringBuilder sb=new StringBuilder(secretKey);
        String temp=EncryptHelper.aesEncrypt(json, NeteaseCloudConfig.presetKey,NeteaseCloudConfig.iv);
        System.out.println(temp);
        System.out.println(secretKey);
        try {
            result.append("params=").append(URLEncoder.encode(EncryptHelper.aesEncrypt(temp, secretKey, NeteaseCloudConfig.iv), "utf-8")).append("&");
            result.append("encSecKey=").append(URLEncoder.encode(EncryptHelper.rsaEncrypt(sb.reverse().toString(), NeteaseCloudConfig.publicKey), "utf-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.toString();

    }

    private String buildLinuxBody(String json){
        StringBuilder result=new StringBuilder();
        return result.append("eparams=").append(EncryptHelper.aesEncrypt(json,NeteaseCloudConfig.linuxApiKey,"")).toString();
    }
}
