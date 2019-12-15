package com.octlr.blog.service.impl;

import com.octlr.blog.entity.Category;
import com.octlr.blog.repository.CategoryRepository;
import com.octlr.blog.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Boolean existCategory(String name) {
        Category category=categoryRepository.findCategoryByName(name);
        return  category!=null?true:false;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
