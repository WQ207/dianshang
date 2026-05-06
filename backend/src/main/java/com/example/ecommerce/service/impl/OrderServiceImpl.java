package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.dto.OrderCreateDTO;
import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.mapper.OrderItemMapper;
import com.example.ecommerce.mapper.OrdersMapper;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public IPage<Orders> page(Page<Orders> page) {
        return ordersMapper.selectPage(page, new LambdaQueryWrapper<Orders>().orderByDesc(Orders::getCreateTime));
    }

    @Override
    public OrderDTO getById(Long id) {
        Orders order = ordersMapper.selectById(id);
        if (order == null) {
            return null;
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        
        List<OrderItem> items = orderItemMapper.selectByOrderId(id);
        List<OrderItemDTO> itemDTOs = new ArrayList<>();
        for (OrderItem item : items) {
            OrderItemDTO itemDTO = new OrderItemDTO();
            BeanUtils.copyProperties(item, itemDTO);
            itemDTOs.add(itemDTO);
        }
        orderDTO.setItems(itemDTOs);
        return orderDTO;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        Orders order = new Orders();
        order.setId(id);
        order.setStatus(status);
        return ordersMapper.updateById(order) > 0;
    }

    @Override
    public List<Orders> list() {
        return ordersMapper.selectList(new LambdaQueryWrapper<Orders>().orderByDesc(Orders::getCreateTime));
    }

    @Override
    public String createOrder(OrderCreateDTO orderCreateDTO) {
        String orderNo = "ORD" + System.currentTimeMillis();
        
        Orders order = new Orders();
        order.setOrderNo(orderNo);
        order.setUserId(orderCreateDTO.getUserId());
        order.setTotalAmount(orderCreateDTO.getTotalAmount());
        order.setStatus(1);
        order.setReceiver(orderCreateDTO.getReceiver());
        order.setPhone(orderCreateDTO.getPhone());
        order.setAddress(orderCreateDTO.getAddress());
        ordersMapper.insert(order);
        
        Long orderId = order.getId();
        
        for (OrderCreateDTO.OrderItemDTO itemDTO : orderCreateDTO.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setProductId(itemDTO.getProductId());
            item.setProductName(itemDTO.getProductName());
            item.setPrice(itemDTO.getPrice());
            item.setQuantity(itemDTO.getQuantity());
            item.setTotalPrice(itemDTO.getTotalPrice());
            orderItemMapper.insert(item);
        }
        
        return orderNo;
    }
}