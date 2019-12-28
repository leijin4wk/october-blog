package com.octlr.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.octlr.blog.config.NeteaseCloudConfig;
import com.octlr.blog.config.SysConfig;
import com.octlr.blog.service.NeteaseCloudMusicService;
import com.octlr.blog.util.EncryptHelper;
import com.octlr.blog.vo.SongDetailVo;
import com.octlr.blog.vo.SongUrlVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

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

    @Override
    public List<SongDetailVo> getLikeList() {
        log.info("getLikeList start");
        String value = redisTemplate.opsForValue().get("octlr:NeteaseCloudMusic:profile");
        if (StringUtils.isEmpty(value)) {
            log.info("更新token和cookie");
            value = getProfile();
        }
        JSONObject profile = JSONObject.parseObject(value);
        JSONObject likeListParam = new JSONObject();
        likeListParam.put("uid", profile.getString("userId"));
        ResponseEntity<String> likeListResponseEntity = sendWebRequest(NeteaseCloudConfig.likeList, likeListParam.toJSONString(), new HashMap<>());
        JSONObject songIds = JSONObject.parseObject(likeListResponseEntity.getBody());
        JSONObject detailListParam = new JSONObject();
        JSONArray array = songIds.getJSONArray("ids");
        JSONArray c = new JSONArray();
        array.stream().forEach(item -> {
            JSONObject i = new JSONObject();
            i.put("id", item);
            c.add(i);
        });
        detailListParam.put("ids", songIds.getJSONArray("ids").toJSONString());
        detailListParam.put("c", c.toJSONString());
        ResponseEntity<String> detailResponseEntity = sendWebRequest(NeteaseCloudConfig.songDetail, detailListParam.toJSONString(), new HashMap<>());
        JSONObject songDetail = JSONObject.parseObject(detailResponseEntity.getBody());
        JSONArray jsonArray = songDetail.getJSONArray("songs");
        List<SongDetailVo> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject tmp = jsonArray.getJSONObject(i);
            SongDetailVo songDetailVo = new SongDetailVo();
            songDetailVo.setId(tmp.getLongValue("id"));
            songDetailVo.setName(tmp.getString("name"));
            songDetailVo.setPicUrl(tmp.getJSONObject("al").getString("picUrl"));
            JSONArray ars = tmp.getJSONArray("ar");
            List<String> arNames = new ArrayList<>();
            for (int j = 0; j < ars.size(); j++) {
                JSONObject ar = ars.getJSONObject(j);
                arNames.add(ar.getString("name"));
            }
            songDetailVo.setArName(String.join(",", arNames));
            result.add(songDetailVo);
        }
        log.info("getLikeList end");
        return result;
    }
    @Override
    public SongUrlVo getSongUrl(Long id) {
        log.info("getSongUrl start");
        SongUrlVo songUrlVo=new SongUrlVo();
        JSONObject urlParam = new JSONObject();
        urlParam.put("ids", "[" + id + "]");
        urlParam.put("br", 999000);
        Map<String, String> songUrlCookie = new HashMap<>();
        songUrlCookie.put("os", "pc");
        songUrlCookie.put("_ntes_nuid", EncryptHelper.bytesToHex(EncryptHelper.getRandomString(16).getBytes()));
        ResponseEntity<String> urlResponseEntity = sendLinuxRequest(NeteaseCloudConfig.songUrl, urlParam.toJSONString(), songUrlCookie);
        JSONObject songUrlObj = JSONObject.parseObject(urlResponseEntity.getBody());
        songUrlVo.setMusicUrl(songUrlObj.getJSONArray("data").getJSONObject(0).getString("url"));
        JSONObject lyricParam= new JSONObject();
        lyricParam.put("id",id);
        ResponseEntity<String> lyricResponseEntity = sendLinuxRequest(NeteaseCloudConfig.songLyric, lyricParam.toJSONString(), new HashMap<>());
        JSONObject songLyricObj = JSONObject.parseObject(lyricResponseEntity.getBody());
        songUrlVo.setLyric(songLyricObj.getJSONObject("lrc").getString("lyric"));
        log.info("getSongUrl end");
        return songUrlVo;
    }
    private String getProfile() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", sysConfig.getSysParams().getPhone());
        jsonObject.put("password", DigestUtils.md5Hex(sysConfig.getSysParams().getPassword()));
        jsonObject.put("rememberLogin", "true");
        Map<String, String> cookie = new HashMap<>();
        cookie.put("os", "pc");
        ResponseEntity<String> responseEntity = sendWebRequest(NeteaseCloudConfig.loginCellphone, jsonObject.toJSONString(), cookie);
        return JSON.parseObject(responseEntity.getBody()).getJSONObject("profile").toJSONString();
    }
    private ResponseEntity<String> sendWebRequest(String url, String data, Map<String, String> cookie) {
        String value = redisTemplate.opsForValue().get("octlr:NeteaseCloudMusic:csrf_token");
        if (StringUtils.isEmpty(value)) {
            value = "";
        }
        JSONObject jsonObject = JSON.parseObject(data);
        jsonObject.put("csrf_token", value);
        HttpEntity<String> entity = buildWebHttpEntity(jsonObject.toJSONString(), cookie);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        HttpHeaders httpHeaders = responseEntity.getHeaders();
        List<String> list = httpHeaders.get("set-cookie");
        if (!CollectionUtils.isEmpty(list)) {
            List<String> cookies = new ArrayList<>();
            String pattern = "_csrf=([^(;|$)]+)";
            // 创建 Pattern 对象
            Pattern r = Pattern.compile(pattern);
            for (String item : list) {
                cookies.add(item.replaceFirst("\\s*Domain=[^(;|$)]+;*", ""));
                // 现在创建 matcher 对象
                Matcher m = r.matcher(item);
                if (m.find()) {
                    String tmp = m.group(0);
                    String[] strings = tmp.split("=");
                    if (strings.length == 2) {
                        redisTemplate.opsForValue().set("octlr:NeteaseCloudMusic:csrf_token", strings[1], 7100, TimeUnit.SECONDS);
                    }
                }
            }
            JSONObject profile = JSON.parseObject(responseEntity.getBody()).getJSONObject("profile");
            redisTemplate.opsForValue().set("octlr:NeteaseCloudMusic:cookie", JSON.toJSONString(cookies), 7100, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set("octlr:NeteaseCloudMusic:profile", profile.toJSONString(), 7100, TimeUnit.SECONDS);
        }
        return responseEntity;
    }
    private ResponseEntity<String> sendLinuxRequest(String url, String data, Map<String, String> cookie) {
        JSONObject jsonObject = JSON.parseObject(data);
        jsonObject.put("method", "POST");
        jsonObject.put("url", url);
        jsonObject.put("params", JSON.parseObject(data));
        HttpEntity<String> entity = buildLinuxHttpEntity(jsonObject.toJSONString(), cookie);
        ResponseEntity<String> responseEntity = restTemplate.exchange(NeteaseCloudConfig.linuxforward, HttpMethod.POST, entity, String.class);
        return responseEntity;
    }

    /**
     * @param data
     * @param cookie
     * @return
     */
    private HttpEntity<String> buildWebHttpEntity(String data, Map<String, String> cookie) {
        String cookieCache = redisTemplate.opsForValue().get("octlr:NeteaseCloudMusic:cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.add("referer", "https://music.163.com");
        headers.add("content-type", "application/x-www-form-urlencoded");
        List<String> cookies = new ArrayList<>();
        for (String key : cookie.keySet()) {
            cookies.add(key + "=" + cookie.get(key));
        }
        if (!StringUtils.isEmpty(cookieCache)) {
            cookies.addAll(JSON.parseArray(cookieCache, String.class));
        }
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
        return new HttpEntity<>(buildWebApiBody(data), headers);
    }

    private HttpEntity<String> buildLinuxHttpEntity(String data, Map<String, String> cookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.add("content-type", "application/x-www-form-urlencoded");
        headers.add("referer", "https://music.163.com");
        List<String> cookies = new ArrayList<>();
        for (String key : cookie.keySet()) {
            cookies.add(key + "=" + cookie.get(key));
        }
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.add("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
        return new HttpEntity<>(buildLinuxBody(data), headers);
    }

    private String buildWebApiBody(String json) {
        StringBuilder result = new StringBuilder();
        String secretKey = EncryptHelper.getRandomString(16);
        StringBuilder sb = new StringBuilder(secretKey);
        String temp = EncryptHelper.aesEncryptWithIv(json, NeteaseCloudConfig.presetKey, NeteaseCloudConfig.iv);
        try {
            result.append("params=").append(URLEncoder.encode(EncryptHelper.aesEncryptWithIv(temp, secretKey, NeteaseCloudConfig.iv), "utf-8")).append("&");
            result.append("encSecKey=").append(URLEncoder.encode(EncryptHelper.rsaEncrypt(sb.reverse().toString(), NeteaseCloudConfig.publicKey), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private String buildLinuxBody(String json) {
        StringBuilder result = new StringBuilder();
        try {
            String param=EncryptHelper.aesEncrypt(json, NeteaseCloudConfig.linuxApiKey).toUpperCase();
            return result.append("eparams=").append(param).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
