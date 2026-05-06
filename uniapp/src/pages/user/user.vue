<template>
  <view class="container">
    <view class="user-header">
      <view class="avatar">
        <text class="avatar-icon">👤</text>
      </view>
      <view class="user-info">
        <text class="user-name">用户昵称</text>
        <text class="user-id">ID: 1</text>
      </view>
    </view>
    
    <view class="menu-list">
      <view class="menu-item" @click="goToOrders">
        <text class="menu-icon">📋</text>
        <text class="menu-text">我的订单</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToCart">
        <text class="menu-icon">🛒</text>
        <text class="menu-text">购物车</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item">
        <text class="menu-icon">❤️</text>
        <text class="menu-text">我的收藏</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item">
        <text class="menu-icon">📍</text>
        <text class="menu-text">收货地址</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item">
        <text class="menu-icon">⚙️</text>
        <text class="menu-text">设置</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>
    
    <view class="order-tabs">
      <text class="tabs-title">我的订单</text>
      <view class="tabs-row">
        <view class="tab-item" @click="goToOrders">
          <text class="tab-icon">💳</text>
          <text class="tab-text">待付款</text>
        </view>
        <view class="tab-item" @click="goToOrders">
          <text class="tab-icon">📦</text>
          <text class="tab-text">待发货</text>
        </view>
        <view class="tab-item" @click="goToOrders">
          <text class="tab-icon">🚚</text>
          <text class="tab-text">待收货</text>
        </view>
        <view class="tab-item" @click="goToOrders">
          <text class="tab-icon">⭐</text>
          <text class="tab-text">已完成</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
const goToOrders = () => {
  uni.switchTab({ url: '/pages/orders/orders' })
}

const goToCart = () => {
  uni.switchTab({ url: '/pages/cart/cart' })
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.user-header {
  background: linear-gradient(135deg, #ff6b6b 0%, #ffa502 100%);
  padding: 60rpx 30rpx 40rpx;
  display: flex;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  background-color: rgba(255,255,255,0.3);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  font-size: 60rpx;
}

.user-info {
  margin-left: 30rpx;
}

.user-name {
  display: block;
  font-size: 36rpx;
  color: #fff;
  font-weight: bold;
}

.user-id {
  display: block;
  font-size: 26rpx;
  color: rgba(255,255,255,0.8);
  margin-top: 10rpx;
}

.menu-list {
  margin: 20rpx;
  background-color: #fff;
  border-radius: 15rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
}

.menu-icon {
  font-size: 40rpx;
}

.menu-text {
  flex: 1;
  font-size: 30rpx;
  color: #333;
  margin-left: 20rpx;
}

.menu-arrow {
  font-size: 36rpx;
  color: #ccc;
}

.order-tabs {
  margin: 20rpx;
  background-color: #fff;
  border-radius: 15rpx;
  padding: 20rpx;
}

.tabs-title {
  display: block;
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.tabs-row {
  display: flex;
  justify-content: space-around;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.tab-icon {
  font-size: 48rpx;
}

.tab-text {
  font-size: 24rpx;
  color: #666;
  margin-top: 10rpx;
}
</style>