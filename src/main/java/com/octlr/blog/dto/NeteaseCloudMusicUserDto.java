package com.octlr.blog.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NeteaseCloudMusicUserDto implements Serializable {
    private Long accountId;
    private List<String> cookie;
}

