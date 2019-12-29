package com.octlr.blog.component;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import org.springframework.data.redis.serializer.RedisSerializationContext;


import java.time.Duration;

@Configuration
@Slf4j
public class RedisCacheConfig {
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(){
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer)).entryTtl(Duration.ofSeconds(7200));
        return configuration;
    }

}
