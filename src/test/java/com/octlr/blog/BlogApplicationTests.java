package com.octlr.blog;

import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.entity.Article;
import com.octlr.blog.service.ArticleService;
import com.octlr.blog.util.EncryptHelper;
import com.octlr.blog.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
        String  publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgtQn2JZ34ZC28NWYpAUd98iZ37BUrX/aKzmFbt7clFSs6sXqHauqKWqdtLkF2KexO40H1YTX8z2lSgBBOAxLsvaklV8k4cBFK9snQXE9/DDaFt6Rr7iVZMldczhC0JNgTz+SHXT6CBHuX3e9SdB1Ua44oncaTWz7OBGLbCiK45wIDAQAB";
        String ivstr ="0102030405060708";
        String  presetKey = "0CoJUm6Qyw8W8jud";
        String a="{\"phone\":\"18875016131\",\"password\":\"fb15f40d7db80ac09a91f051b7a30c31\",\"rememberLogin\":\"true\",\"csrf_token\":\"\"}";

        String b="bMRQ7CIu2+d71ojDprDxfNfzN6KxuoYUOVoVeVZPBxno7N+Jg948kDI7nNuV21HGupMK6Cu1Jnj2vTTKznSA/K8Ze9tlbqXTpRFQ8WbRYTCx6r71A+Cqke3kaIqPRwK/08sgkCS91M14T1Ay4dLi6g==";

        String e="6tAvhBr3E4QjJgUu";
        String c="pEUsNlJyvjv/uOjeQB4kH9RQ2JL6/UYyltyVNCTl9iWLHKFCsrqUDgFnVKcdrNRBjYZC29zdYiKs1l3R4VpXMvox5gz84AvTqO+e9FdEfz9qON6iO+EgkMrMjif7ZPfJ5TasNMq1bkzma9wMdUHRrPo83Q+B9oL6Cf5V9x0XVnqVVLGuwxydu0zGov+uaiKtiEtJCQRy8IwlhZNUeHvrBQ==";


        // 格式化key
        SecretKeySpec  key = new SecretKeySpec(presetKey.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(ivstr.getBytes());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); // 确定算法
        cipher.init(Cipher.ENCRYPT_MODE, key,iv);    // 确定密钥
        byte[] result = cipher.doFinal(a.getBytes());  // 加密
        System.out.println(Base64.encodeBase64String(result));  // 不进行Base64编码的话，那么这个字节数组对应的字符串就是乱码
        String d=EncryptHelper.aesEncrypt(a,presetKey,ivstr);
        System.out.println(d);
        String t=EncryptHelper.aesEncrypt(d,e,ivstr);
        System.out.println(t);



    }

    @Test
    public void rsa() throws Exception {
//<Buffer 59 4c 63 49 58 75 45 4c 71 61 58 52 74 66 4e 76>
//594c63495875454c7161585274664e76
//<Buffer 76 4e 66 74 52 58 61 71 4c 45 75 58 49 63 4c 59>
//764e6674525861714c45755849634c59


        String a="SVfnGYAckObPHLff";
        byte[] bb=EncryptHelper.hexStringToByteArray("594c63495875454c7161585274664e76");
        String b="ffLHPbOkcAYGnfVS";
        System.out.println();
    }
}
