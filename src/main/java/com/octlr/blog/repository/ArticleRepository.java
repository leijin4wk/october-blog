package com.octlr.blog.repository;

import com.octlr.blog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Integer> {
}
