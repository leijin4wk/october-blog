package com.octlr.blog.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.octlr.blog.config.OssConfig;
import com.octlr.blog.dto.OssDto;
import com.octlr.blog.entity.Article;
import com.octlr.blog.entity.Classify;
import com.octlr.blog.repository.ArticleRepository;
import com.octlr.blog.repository.ClassifyRepository;
import com.octlr.blog.utils.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


@Component
@Slf4j
public class InitDBData implements ApplicationRunner {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ClassifyRepository classifyRepository;
    @Autowired
    private OssConfig ossConfig;
    private String jsonFileName = "info.json";

    @Override
    public void run(ApplicationArguments args) {
        OssDto ossDto = OssUtils.readProperty(ossConfig.getLocalConfigPath());
        File file = new File(ossConfig.getLocalArticlePath());
        Iterator<File> iterator = FileUtils.iterateFiles(file, new FileFileFilter() {
            public boolean accept(File file) {
                return jsonFileName.equals(file.getName());
            }
        }, new DirectoryFileFilter() {
            public boolean accept(File file) {
                return true;
            }
        });
        List<Article> list=new ArrayList<>();
        while (iterator.hasNext()) {
            try {
                String json = FileUtils.readFileToString(iterator.next(), "utf-8");
                JSONObject jsonObject = JSON.parseObject(json);
                Classify classify = saveClassify(jsonObject);
                list.addAll(buildArticleList(jsonObject, classify.getId()));
            } catch (IOException e) {
                e.printStackTrace();
                log.error("json文件读取失败！");
            }
        }
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(item->{
                OssUtils.uploadFile(ossDto, ossConfig.getLocalDbPath(),item.getUrl());
            });
            OssUtils.uploadFile(ossDto, ossConfig.getLocalDbPath(), ossConfig.getRemoteDbName());
        }
        log.info("发布成功！");
    }

    public Classify saveClassify(JSONObject jsonObject) {
        Classify classify = Classify.builder().name(jsonObject.getString("classifyName"))
                .build();
        classifyRepository.save(classify);
        log.info(classify.toString());
        return classify;
    }

    public List<Article> buildArticleList(JSONObject jsonObject, Integer classifyId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Article> list = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("articleList");
        for (int i = 0; i < jsonArray.size(); i++) {
            Article article = new Article();
            JSONObject item = jsonArray.getJSONObject(i);
            File file = new File(ossConfig.getLocalArticlePath() + "/" + item.getString("filePath"));
            article.setClassifyId(classifyId);
            article.setTitle(item.getString("title"));
            article.setOutline(item.getString("outline"));
            article.setUrl(ossConfig.getRemoteArticleDir() + "/" + item.getString("filePath"));
            try {
                article.setCreateTime(simpleDateFormat.parse(item.getString("createTime")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            article.setUpdateTime(new Date(file.lastModified()));
            log.info(article.toString());
            list.add(article);
        }
        articleRepository.saveAll(list);
        return list;
    }
}
