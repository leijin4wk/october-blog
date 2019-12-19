package com.octlr.blog.service.impl;

import com.octlr.blog.common.BaseException;
import com.octlr.blog.common.BasePageResponse;
import com.octlr.blog.entity.Article;
import com.octlr.blog.entity.Category;
import com.octlr.blog.repository.ArticleRepository;
import com.octlr.blog.repository.CategoryRepository;
import com.octlr.blog.service.ArticleService;
import com.octlr.blog.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public BasePageResponse<Article> findArticleByPage(Integer pageNum, Integer pageSize,Integer categoryId) {
        if (categoryId==null){
            Page<Article> page= articleRepository.findByPage(PageRequest.of(pageNum,pageSize));
            return BasePageResponse.<Article>builder().pageNum(pageNum).pageSize(pageSize)
                    .total(page.getTotalElements()).totalPages(page.getTotalPages())
                    .content(page.getContent()).build();
        }else{
            Page<Article> page= articleRepository.findByCategoryId(categoryId,PageRequest.of(pageNum,pageSize));
            return BasePageResponse.<Article>builder().pageNum(pageNum).pageSize(pageSize)
                    .total(page.getTotalElements()).totalPages(page.getTotalPages())
                    .content(page.getContent()).build();
        }
    }

    @Override
    public ArticleVo findArticleById(Integer id) {
        Article article=articleRepository.findById(id).get();
        if (article==null){
            throw new BaseException("查询文章不存在！");
        }
        Category category= categoryRepository.findById(article.getCategoryId()).get();
        ArticleVo articleVo=new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCategoryName(category.getName());
        return articleVo;
    }

    @Override
    public void saveArticle(Article article) {
        articleRepository.save(article);
    }
}
