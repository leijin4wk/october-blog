package com.octlr.blog.controller;

import com.octlr.blog.common.BaseResponse;
import com.octlr.blog.entity.Category;
import com.octlr.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/search")
    public BaseResponse<List<Category>> findAll()
    {
        return BaseResponse.success(categoryService.findAll());
    }
    @GetMapping("/exist")
    public BaseResponse<Boolean> existArticle( @RequestParam String name)
    {
        return BaseResponse.success(categoryService.existCategory(name));
    }
    @PostMapping("/add")
    public BaseResponse<Category> addArticle(@RequestBody Category category)
    {
        category.setCreateTime(new Date());
        return BaseResponse.success(categoryService.saveCategory(category));
    }
}
