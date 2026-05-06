package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.dto.OrderCreateDTO;
import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.entity.Orders;

import java.util.List;

public interface OrderService {
    IPage<Orders> page(Page<Orders> page);
    OrderDTO getById(Long id);
    boolean updateStatus(Long id, Integer status);
    List<Orders> list();
    String createOrder(OrderCreateDTO orderCreateDTO);
}