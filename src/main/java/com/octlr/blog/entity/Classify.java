package com.octlr.blog.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
public class Classify {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
}
