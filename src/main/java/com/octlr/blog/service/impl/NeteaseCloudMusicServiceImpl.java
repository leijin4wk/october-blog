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
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class NeteaseCloudMusicServiceImpl implements NeteaseCloudMusicService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SysConfig sysConfig;
//sysConfig.getSysParams().getPhone() + "&password=" + sysConfig.getSysParams().getPassword()
    @Override
    public String getProfile(){
        String value=redisTemplate.opsForValue().get("octlr:NeteaseCloudMusic:profile");
        if (!StringUtils.isEmpty(value)){
            return value;
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("phone",sysConfig.getSysParams().getPhone());
        jsonObject.put("password", DigestUtils.md5Hex(sysConfig.getSysParams().getPassword()));
        jsonObject.put("rememberLogin","true");
        Map<String,String> cookie=new HashMap<>();
        cookie.put("os","pc");
        sendRequest(NeteaseCloudConfig.loginCellphone,1,jsonObject.toJSONString(),cookie);
        return redisTemplate.opsForValue().get("octlr:NeteaseCloudMusic:profile");

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
        String value=redisTemplate.opsForValue().get("octlr:NeteaseCloudMusic:csrf_token");
        if (StringUtils.isEmpty(value)){
            value="";
        }
        HttpEntity<String> entity;
        if (apiType==1) {
            JSONObject jsonObject=JSON.parseObject(data);
            jsonObject.put("csrf_token",value);
            entity = buildHttpEntity(apiType,jsonObject.toJSONString(),cookie);
        }else{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("method","POST");
            jsonObject.put("url",url);
            jsonObject.put("params",JSON.parseObject(data));
            entity = buildHttpEntity(apiType,jsonObject.toJSONString(),cookie);
        }
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        HttpHeaders httpHeaders= responseEntity.getHeaders();
        List<String> list=httpHeaders.get("set-cookie");
        if(!CollectionUtils.isEmpty(list)) {
            List<String> cookies = new ArrayList<>();
            String pattern = "_csrf=([^(;|$)]+)";
            // 创建 Pattern 对象
            Pattern r = Pattern.compile(pattern);
            for (String item : list) {
                cookies.add(item.replaceFirst("\\s*Domain=[^(;|$)]+;*", ""));
                // 现在创建 matcher 对象
                Matcher m = r.matcher(item);
                if (m.find()) {
                   String tmp= m.group(0);
                   String[] strings= tmp.split("=");
                   if (strings.length==2) {
                       redisTemplate.opsForValue().set("octlr:NeteaseCloudMusic:csrf_token", strings[1], 7100, TimeUnit.SECONDS);
                   }
                }
            }
            String body=responseEntity.getBody();
            JSONObject jsonObject=JSON.parseObject(body);
            JSONObject profile=jsonObject.getJSONObject("profile");
            redisTemplate.opsForValue().set("octlr:NeteaseCloudMusic:cookie", JSON.toJSONString(cookies), 7100, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set("octlr:NeteaseCloudMusic:profile", profile.toJSONString(), 7100, TimeUnit.SECONDS);
        }
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
        String cookieCache=redisTemplate.opsForValue().get("octlr:NeteaseCloudMusic:cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept","*/*");
        headers.add("referer","https://music.163.com");
        headers.add("content-type","application/x-www-form-urlencoded");
        List<String> cookies =new ArrayList<>();
        for (String key:cookie.keySet()) {
            cookies.add(key+"="+cookie.get(key));
        }
        if (!StringUtils.isEmpty(cookieCache)){
            cookies.addAll(JSON.parseArray(cookieCache,String.class));
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
        StringBuilder result=new StringBuilder();
        String secretKey= EncryptHelper.getRandomString(16);
        StringBuilder sb=new StringBuilder(secretKey);
        String temp=EncryptHelper.aesEncrypt(json, NeteaseCloudConfig.presetKey,NeteaseCloudConfig.iv);
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
