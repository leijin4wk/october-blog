package com.octlr.blog.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class ArticleVo implements Serializable {
    private Integer id;

    private Integer categoryId;

    private String categoryName;

    private String title;

    private String description;

    private String content;

    private Date createTime;

    private Date updateTime;
}
