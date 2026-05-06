package com.example.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateDTO {
    
    private Long userId;
    
    private List<OrderItemDTO> items;
    
    private BigDecimal totalAmount;
    
    private String receiver;
    
    private String phone;
    
    private String address;
    
    @Data
    public static class OrderItemDTO {
        private Long productId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal totalPrice;
    }
}