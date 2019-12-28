package com.octlr.blog.service;

import com.octlr.blog.dto.NeteaseCloudMusicUserDto;

public interface NeteaseCloudMusicService {

    NeteaseCloudMusicUserDto getNeteaseCloudMusicCooike();

    void clearCookie();

    void loginByCellPhone();

}
