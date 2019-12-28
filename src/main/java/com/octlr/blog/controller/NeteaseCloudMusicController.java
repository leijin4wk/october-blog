package com.octlr.blog.controller;

import com.octlr.blog.common.BaseResponse;
import com.octlr.blog.service.NeteaseCloudMusicService;
import com.octlr.blog.vo.SongDetailVo;
import com.octlr.blog.vo.SongUrlVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/music")
public class NeteaseCloudMusicController {
    @Autowired
    private NeteaseCloudMusicService neteaseCloudMusicService;

    @GetMapping("/getLikeList")
    public BaseResponse<List<SongDetailVo>>  getLikeList(){
        return BaseResponse.success(neteaseCloudMusicService.getLikeList());
    }
    @GetMapping("/getSongUrl")
    public BaseResponse<SongUrlVo>  getSongUrl(@RequestParam("id") Long id){
        return BaseResponse.success(neteaseCloudMusicService.getSongUrl(id));
    }
}
