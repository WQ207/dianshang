package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.common.PageResult;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/page")
    public Result<PageResult<Product>> page(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String name) {
        IPage<Product> productPage = productService.page(new Page<>(page, size), name);
        PageResult<Product> result = new PageResult<>();
        result.setRecords(productPage.getRecords());
        result.setTotal(productPage.getTotal());
        result.setCurrent(productPage.getCurrent());
        result.setSize(productPage.getSize());
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return Result.success(product);
    }

    @GetMapping("/list")
    public Result<List<Product>> list() {
        List<Product> products = productService.list();
        return Result.success(products);
    }

    @PostMapping
    public Result<Void> save(@RequestBody ProductDTO productDTO) {
        productService.save(productDTO);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody ProductDTO productDTO) {
        productService.update(productDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success();
    }
}