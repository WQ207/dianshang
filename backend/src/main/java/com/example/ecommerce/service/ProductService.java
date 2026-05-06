package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Product;

import java.util.List;

public interface ProductService {
    IPage<Product> page(Page<Product> page, String name);
    Product getById(Long id);
    boolean save(ProductDTO productDTO);
    boolean update(ProductDTO productDTO);
    boolean delete(Long id);
    List<Product> list();
}