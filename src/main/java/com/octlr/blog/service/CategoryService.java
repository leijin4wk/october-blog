package com.octlr.blog.service;

import com.octlr.blog.entity.Category;

import java.util.List;

public interface CategoryService {

    Category  saveCategory(Category category);

    List<Category> findAll();

}
