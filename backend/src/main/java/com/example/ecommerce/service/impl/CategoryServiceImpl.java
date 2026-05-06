package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.entity.ProductCategory;
import com.example.ecommerce.mapper.ProductCategoryMapper;
import com.example.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ProductCategoryMapper categoryMapper;

    @Override
    public List<ProductCategory> list() {
        return categoryMapper.selectList(new LambdaQueryWrapper<ProductCategory>().orderByAsc(ProductCategory::getSort));
    }

    @Override
    public ProductCategory getById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public boolean save(ProductCategory category) {
        return categoryMapper.insert(category) > 0;
    }

    @Override
    public boolean update(ProductCategory category) {
        return categoryMapper.updateById(category) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return categoryMapper.deleteById(id) > 0;
    }
}