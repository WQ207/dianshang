package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public IPage<Product> page(Page<Product> page, String name) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            queryWrapper.like(Product::getName, name);
        }
        return productMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public boolean save(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return productMapper.insert(product) > 0;
    }

    @Override
    public boolean update(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return productMapper.updateById(product) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return productMapper.deleteById(id) > 0;
    }

    @Override
    public List<Product> list() {
        return productMapper.selectList(null);
    }
}