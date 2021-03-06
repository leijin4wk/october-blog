package com.octlr.blog.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Article {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private Integer categoryId;

    private String title;

    private String description;

    private String content;

    private Date createTime;

    private Date updateTime;
}
