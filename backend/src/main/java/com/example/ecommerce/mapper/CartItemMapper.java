package com.example.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ecommerce.entity.CartItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
    
    @Select("SELECT * FROM cart_item WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<CartItem> selectByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM cart_item WHERE user_id = #{userId} AND product_id = #{productId}")
    CartItem selectByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
    
    @Delete("DELETE FROM cart_item WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") Long userId);
    
    @Delete("DELETE FROM cart_item WHERE user_id = #{userId} AND product_id = #{productId}")
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}