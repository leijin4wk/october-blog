package com.octlr.blog.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class SongUrlVo  implements Serializable {
    private String musicUrl;
    private String lyric;
}
