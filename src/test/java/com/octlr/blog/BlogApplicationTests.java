package com.octlr.blog;

import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.entity.Article;
import com.octlr.blog.service.ArticleService;
import com.octlr.blog.service.NeteaseCloudMusicService;
import com.octlr.blog.util.EncryptHelper;
import com.octlr.blog.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
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

    private String baseUrl="http://127.0.0.1:3000";

    @Autowired
    private NeteaseCloudMusicService neteaseCloudMusicService;
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

   //http://127.0.0.1:4000/login/cellphone?phone=xxxx&password=xxx
    @Test
    public void loginNeteaseCloudMusic(){
        String uri=baseUrl+"/login/cellphone?phone=18875016131&password=3880428a";
        System.out.println(uri);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity r=restTemplate.exchange(uri, HttpMethod.GET, entity,String.class);
        System.out.println(r);
    }
    @Test
    public void md5(){
        String a="fb15f40d7db80ac09a91f051b7a30c31";
        System.out.println(DigestUtils.md5Hex("3880428a"));
    }
    @Test
    public void aes() throws Exception {
       String ivstr ="0102030405060708";
        String  presetKey = "0CoJUm6Qyw8W8jud";
        String a="{\"phone\":\"18875016131\",\"password\":\"fb15f40d7db80ac09a91f051b7a30c31\",\"rememberLogin\":\"true\",\"csrf_token\":\"\"}";
        String e="6tAvhBr3E4QjJgUu";
        String d=EncryptHelper.aesEncrypt(a,presetKey,ivstr);
        String t=EncryptHelper.aesEncrypt(d,e,ivstr);
        System.out.println(t);
    }

    @Test
    public void rsa() throws Exception {
//FSxBW1yMY7wyOFUe
//92ebf29e2264f5cf61c418aa88de1fb06ad7b789b6ac9762a09047fa42951ed3c18264268cc1090225fd189e8c8ef3a32673cb4f7a6e34e12575d6e8ec3fb84c81d10b3f8f9b807f5bbb4c0155ab2927ed08ff58bd8df23d4be18b1c6ee1ca935bc0736ebc0883f00dfc97d83f477992ca895494edf5ff7f0c13ca90695c7569
        String  publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgtQn2JZ34ZC28NWYpAUd98iZ37BUrX/aKzmFbt7clFSs6sXqHauqKWqdtLkF2KexO40H1YTX8z2lSgBBOAxLsvaklV8k4cBFK9snQXE9/DDaFt6Rr7iVZMldczhC0JNgTz+SHXT6CBHuX3e9SdB1Ua44oncaTWz7OBGLbCiK45wIDAQAB";
        String a="FSxBW1yMY7wyOFUe";
        String bb=EncryptHelper.rsaEncrypt(a,publicKey);
        System.out.println(bb);

    }
    @Test
    public void login()  {
        neteaseCloudMusicService.loginByCellPhone();
    }
}
