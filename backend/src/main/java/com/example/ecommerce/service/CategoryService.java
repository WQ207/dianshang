package com.example.ecommerce.service;

import com.example.ecommerce.entity.ProductCategory;

import java.util.List;

public interface CategoryService {
    List<ProductCategory> list();
    ProductCategory getById(Long id);
    boolean save(ProductCategory category);
    boolean update(ProductCategory category);
    boolean delete(Long id);
}