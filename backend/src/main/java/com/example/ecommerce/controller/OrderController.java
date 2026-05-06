package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.common.PageResult;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.dto.OrderCreateDTO;
import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/page")
    public Result<PageResult<Orders>> page(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        IPage<Orders> orderPage = orderService.page(new Page<>(page, size));
        PageResult<Orders> result = new PageResult<>();
        result.setRecords(orderPage.getRecords());
        result.setTotal(orderPage.getTotal());
        result.setCurrent(orderPage.getCurrent());
        result.setSize(orderPage.getSize());
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<OrderDTO> getById(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getById(id);
        return Result.success(orderDTO);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        orderService.updateStatus(id, status);
        return Result.success();
    }

    @GetMapping("/status/options")
    public Result<Map<String, String>> getStatusOptions() {
        Map<String, String> options = new HashMap<>();
        options.put("0", "待付款");
        options.put("1", "已付款");
        options.put("2", "已发货");
        options.put("3", "已完成");
        options.put("4", "已取消");
        return Result.success(options);
    }

    @PostMapping
    public Result<Map<String, String>> createOrder(@RequestBody OrderCreateDTO orderCreateDTO) {
        String orderNo = orderService.createOrder(orderCreateDTO);
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", orderNo);
        return Result.success(result);
    }
}