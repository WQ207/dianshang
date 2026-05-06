<template>
  <view class="container">
    <view v-if="cartItems.length > 0" class="cart-list">
      <view v-for="item in cartItems" :key="item.productId" class="cart-item">
        <image class="item-image" :src="item.image || '/static/default.png'" mode="aspectFill" />
        <view class="item-info">
          <text class="item-name">{{ item.productName }}</text>
          <text class="item-price">¥{{ item.price }}</text>
          <view class="item-control">
            <view class="qty-btn" @click="decreaseQty(item)">-</view>
            <text class="qty-value">{{ item.quantity }}</text>
            <view class="qty-btn" @click="increaseQty(item)">+</view>
          </view>
        </view>
        <view class="item-delete" @click="deleteItem(item)">删除</view>
      </view>
    </view>
    
    <view v-else class="empty-cart">
      <text class="empty-icon">🛒</text>
      <text class="empty-text">购物车是空的</text>
      <view class="empty-btn" @click="goToIndex">去逛逛</view>
    </view>
    
    <view v-if="cartItems.length > 0" class="bottom-bar">
      <view class="total-info">
        <text class="total-label">合计:</text>
        <text class="total-price">¥{{ totalAmount.toFixed(2) }}</text>
      </view>
      <view class="checkout-btn" @click="goToCheckout">去结算({{ totalCount }})</view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

interface CartItem {
  productId: number
  productName: string
  price: number
  quantity: number
  image?: string
}

const cartItems = ref<CartItem[]>([])

const totalAmount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const totalCount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
})

const loadCartItems = () => {
  const stored = uni.getStorageSync('cartItems')
  if (stored) {
    try {
      cartItems.value = JSON.parse(stored)
    } catch (e) {
      cartItems.value = []
    }
  }
}

const saveCartItems = () => {
  uni.setStorageSync('cartItems', JSON.stringify(cartItems.value))
}

const decreaseQty = (item: CartItem) => {
  if (item.quantity > 1) {
    item.quantity--
    saveCartItems()
  }
}

const increaseQty = (item: CartItem) => {
  item.quantity++
  saveCartItems()
}

const deleteItem = (item: CartItem) => {
  const index = cartItems.value.findIndex(i => i.productId === item.productId)
  if (index > -1) {
    cartItems.value.splice(index, 1)
    saveCartItems()
    uni.showToast({ title: '删除成功', icon: 'success' })
  }
}

const goToIndex = () => {
  uni.switchTab({ url: '/pages/index/index' })
}

const goToCheckout = () => {
  uni.setStorageSync('checkoutItems', JSON.stringify(cartItems.value))
  uni.navigateTo({ url: '/pages/checkout/checkout' })
}

onMounted(() => {
  loadCartItems()
})
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 140rpx;
}

.cart-list {
  padding: 20rpx;
}

.cart-item {
  display: flex;
  background-color: #fff;
  border-radius: 15rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.item-image {
  width: 180rpx;
  height: 180rpx;
  border-radius: 10rpx;
}

.item-info {
  flex: 1;
  padding: 0 20rpx;
}

.item-name {
  display: block;
  font-size: 30rpx;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-price {
  display: block;
  font-size: 32rpx;
  color: #ff6b6b;
  font-weight: bold;
  margin-top: 15rpx;
}

.item-control {
  display: flex;
  align-items: center;
  gap: 20rpx;
  margin-top: 20rpx;
}

.qty-btn {
  width: 50rpx;
  height: 50rpx;
  line-height: 50rpx;
  text-align: center;
  background-color: #f5f5f5;
  border-radius: 50%;
  font-size: 32rpx;
}

.qty-value {
  font-size: 28rpx;
  min-width: 50rpx;
  text-align: center;
}

.item-delete {
  color: #999;
  font-size: 26rpx;
  padding: 0 20rpx;
}

.empty-cart {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 30rpx;
  color: #999;
  margin-bottom: 30rpx;
}

.empty-btn {
  padding: 20rpx 60rpx;
  background-color: #ff6b6b;
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0,0,0,0.1);
}

.total-info {
  display: flex;
  align-items: baseline;
}

.total-label {
  font-size: 28rpx;
  color: #666;
}

.total-price {
  font-size: 40rpx;
  color: #ff6b6b;
  font-weight: bold;
  margin-left: 10rpx;
}

.checkout-btn {
  padding: 20rpx 60rpx;
  background-color: #ff6b6b;
  color: #fff;
  border-radius: 40rpx;
  font-size: 30rpx;
}
</style>