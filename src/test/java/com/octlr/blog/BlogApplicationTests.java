package com.octlr.blog;

import com.alibaba.fastjson.JSONObject;
import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.entity.Article;
import com.octlr.blog.service.ArticleService;
import com.octlr.blog.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class BlogApplicationTests {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RestTemplate restTemplate;

    private String baseUrl="http://127.0.0.1:4000";
    @Test
   public void findByPage() {
        BasePageResponse<Article> pageResponse = articleService.findArticleByPage( 0, 10,null);
        System.out.println(pageResponse);
    }

    @Test
    public void findById() {
        ArticleVo article = articleService.findArticleById(1);
        System.out.println(article);
    }
    @Test
    public void redisTemplate() {
        stringRedisTemplate.opsForValue().set("aaa","222",20, TimeUnit.SECONDS);
    }
    @Test
    public void rustTemplate(){
        String uri="http://wthrcdn.etouch.cn/weather_mini?city=上海";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String strbody=restTemplate.exchange(uri, HttpMethod.GET, entity,String.class).getBody();
        System.out.println(strbody);
    }

   //http://127.0.0.1:4000/login/cellphone?phone=18875016131&password=3880428a
    @Test
    public void loginNeteaseCloudMusic(){
        String uri=baseUrl+"/login/cellphone?phone=xxxx&password=xxx";
        System.out.println(uri);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity r=restTemplate.exchange(uri, HttpMethod.GET, entity,String.class);
        System.out.println(r);
    }
}
