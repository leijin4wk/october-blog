package com.octlr.blog.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Article {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private Integer classifyId;

    private String title;

    private String outline;

    private String url;

    private Date createTime;

    private Date updateTime;
}
