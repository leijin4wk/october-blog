package com.octlr.blog.controller;

import com.alibaba.fastjson.JSON;
import com.octlr.blog.common.BaseResponse;
import com.octlr.blog.dto.NeteaseCloudMusicUserDto;
import com.octlr.blog.service.NeteaseCloudMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/music")
public class NeteaseCloudMusicController {
    @Autowired
    private NeteaseCloudMusicService neteaseCloudMusicService;
    @GetMapping("/getCookie")
    public ResponseEntity<String> getCookie()
    {
        HttpHeaders headers=new HttpHeaders();
        NeteaseCloudMusicUserDto neteaseCloudMusicUserDto=neteaseCloudMusicService.getNeteaseCloudMusicCooike();
        headers.addAll("set-cookie",neteaseCloudMusicUserDto.getCookie());
        return ResponseEntity.status(200).headers(headers).body(JSON.toJSONString(BaseResponse.success(neteaseCloudMusicUserDto.getAccountId())));
    }
    @GetMapping("/clear")
    public BaseResponse<String> findAll()
    {
        neteaseCloudMusicService.clearCookie();
        return BaseResponse.success("ok");
    }
}
