package com.octlr.blog.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class BasePageResponse<T> {

    private Integer totalPages;

    private Long total;

    private Integer pageNum;

    private Integer pageSize;

    private List<T> content;
}
