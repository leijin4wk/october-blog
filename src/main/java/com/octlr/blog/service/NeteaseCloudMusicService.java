package com.octlr.blog.service;

import com.octlr.blog.vo.SongDetailVo;
import com.octlr.blog.vo.SongUrlVo;

import java.util.List;

public interface NeteaseCloudMusicService {

    List<SongDetailVo> getLikeList();

    SongUrlVo getSongUrl(Long id);

}
