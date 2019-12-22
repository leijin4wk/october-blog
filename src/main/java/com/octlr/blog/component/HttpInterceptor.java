package com.octlr.blog.component;

import com.alibaba.fastjson.JSON;
import com.octlr.blog.common.BaseResponse;
import com.octlr.blog.common.CodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {
    private String authTokenHeadKey="octlr-token";
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    private String authToken;
    public HttpInterceptor(String authToken) {
        this.authToken = authToken;
    }

    private List<String>  getExclusionsPath(){
        List<String> list=new ArrayList<>();
        list.add("/api/");
        list.add("/api/error");
        list.add("/api/favicon.ico");
        list.add("/api/article/search");
        list.add("/api/article/search/*");
        list.add("/api/article/category");
        list.add("/api/category/search");
        list.add("/api/music/getCookie");
        list.add("/api/music/clear");
        return list;
    }
    private boolean matchExclusionsPath(String path){
        List<String> list= getExclusionsPath();
        for(int i=0;i<list.size();i++){
            if (antPathMatcher.match(list.get(i),path)){
                return true;
            }
        }
        return false;
    }
    private boolean matchToken(HttpServletRequest request){
       String token= request.getHeader(authTokenHeadKey);
        return authToken.equals(token);
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //请求时间和请求参数
       String url= request.getRequestURI();
       log.info("access:{}",url);
       if (matchExclusionsPath(url)){
           return true;
       }
       if (matchToken(request)){
           return true;
       }
       log.info("没有访问权限:{}",url);
       //重置response
       response.reset();
       response.setCharacterEncoding("UTF-8");
       response.setContentType("application/json;charset=UTF-8");
       PrintWriter pw = response.getWriter();
       pw.write(JSON.toJSONString(BaseResponse.error(CodeMsg.AuthError)));
       pw.flush();
       pw.close();
       return false;
    }
}