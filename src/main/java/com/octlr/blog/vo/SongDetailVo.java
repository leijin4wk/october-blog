package com.octlr.blog.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class SongDetailVo  implements Serializable {
    private Long id;
    private String name;
    private String arName;
    private String picUlr;
}
