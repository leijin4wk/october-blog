package com.octlr.blog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OssDto {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
