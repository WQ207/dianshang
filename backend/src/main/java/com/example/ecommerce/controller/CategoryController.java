package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.ProductCategory;
import com.example.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<ProductCategory>> list() {
        List<ProductCategory> categories = categoryService.list();
        return Result.success(categories);
    }

    @GetMapping("/{id}")
    public Result<ProductCategory> getById(@PathVariable Long id) {
        ProductCategory category = categoryService.getById(id);
        return Result.success(category);
    }

    @PostMapping
    public Result<Void> save(@RequestBody ProductCategory category) {
        categoryService.save(category);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody ProductCategory category) {
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success();
    }
}