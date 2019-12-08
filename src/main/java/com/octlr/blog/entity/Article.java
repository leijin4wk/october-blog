package com.octlr.blog.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@Builder
public class Article {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer classifyId;

    private String title;

    private String outline;

    private Date createTime;

    private Date updateTime;
}
