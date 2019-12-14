package com.octlr.blog.repository;

import com.octlr.blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article,Integer> {
    @Query(value = "SELECT * FROM Article WHERE title like %?1% or content  like %?1%",
            countQuery = "SELECT count(*) FROM Article WHERE title like %?1% or content  like %?1%",
            nativeQuery = true)
    Page<Article> findByPage(String querySearch, Pageable pageable);
}
