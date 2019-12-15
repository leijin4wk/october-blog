package com.octlr.blog.repository;

import com.octlr.blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article,Integer> {
    @Query(value = "SELECT * FROM Article",
            countQuery = "SELECT count(*) FROM Article",
            nativeQuery = true)
    Page<Article> findByPage(Pageable pageable);

    @Query(value = "SELECT * FROM Article WHERE category_id =?1",
            countQuery = "SELECT count(*) FROM Article WHERE category_id =?1",
            nativeQuery = true)
    Page<Article> findByCategoryId(Integer categoryId, Pageable pageable);
}
