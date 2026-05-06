<template>
  <view class="container">
    <view v-if="product" class="product-detail">
      <!-- 商品图片 -->
      <image class="product-image" :src="product.image || '/static/default.png'" mode="aspectFill" />
      
      <!-- 商品信息 -->
      <view class="product-header">
        <text class="product-name">{{ product.name }}</text>
        <text class="product-price">¥{{ product.price }}</text>
      </view>
      
      <view class="product-desc">
        <text>{{ product.description }}</text>
      </view>
      
      <view class="product-info">
        <view class="info-row">
          <text class="info-label">库存</text>
          <text class="info-value">{{ product.stock }} 件</text>
        </view>
        <view class="info-row">
          <text class="info-label">分类</text>
          <text class="info-value">{{ categoryName }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">状态</text>
          <text class="info-value" :class="product.status === 1 ? 'status-active' : 'status-inactive'">
            {{ product.status === 1 ? '上架' : '下架' }}
          </text>
        </view>
      </view>
      
      <!-- 数量选择 -->
      <view class="quantity-select">
        <text class="quantity-label">数量</text>
        <view class="quantity-control">
          <view class="qty-btn" @click="decreaseQty">-</view>
          <text class="qty-value">{{ quantity }}</text>
          <view class="qty-btn" @click="increaseQty">+</view>
        </view>
      </view>
    </view>
    
    <!-- 底部操作栏 -->
    <view class="bottom-bar">
      <view class="bar-left">
        <view class="bar-item" @click="goToCart">
          <text class="bar-icon">🛒</text>
          <text class="bar-text">购物车</text>
        </view>
        <view class="bar-item" @click="goToOrders">
          <text class="bar-icon">📋</text>
          <text class="bar-text">订单</text>
        </view>
      </view>
      <view class="bar-right">
        <view class="btn-cart" @click="addToCart">加入购物车</view>
        <view class="btn-buy" @click="buyNow">立即购买</view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

const mockProducts = [
  { id: 1, name: 'iPhone 15', price: 5999, stock: 100, categoryId: 1, image: '', description: '最新款苹果手机，搭载A17芯片', status: 1 },
  { id: 2, name: 'MacBook Pro', price: 14999, stock: 50, categoryId: 1, image: '', description: '专业级笔记本电脑，M3芯片', status: 1 },
  { id: 3, name: 'AirPods Pro', price: 1899, stock: 200, categoryId: 1, image: '', description: '主动降噪蓝牙耳机', status: 1 },
  { id: 4, name: '男士纯棉T恤', price: 99, stock: 500, categoryId: 2, image: '', description: '100%纯棉面料，舒适透气', status: 1 },
  { id: 5, name: '运动跑鞋', price: 299, stock: 300, categoryId: 2, image: '', description: '轻便透气，适合各种运动', status: 1 },
  { id: 6, name: '休闲牛仔裤', price: 199, stock: 400, categoryId: 2, image: '', description: '经典版型，百搭时尚', status: 1 },
  { id: 7, name: '进口巧克力', price: 68, stock: 150, categoryId: 3, image: '', description: '比利时进口，丝滑口感', status: 1 },
  { id: 8, name: '坚果礼盒', price: 128, stock: 80, categoryId: 3, image: '', description: '精选坚果，营养健康', status: 1 },
  { id: 9, name: 'Java编程思想', price: 89, stock: 200, categoryId: 4, image: '', description: 'Java经典教材', status: 1 },
  { id: 10, name: 'Vue3实战', price: 79, stock: 150, categoryId: 4, image: '', description: 'Vue3开发实战指南', status: 1 }
]

const mockCategories = [
  { id: 1, name: '电子产品' },
  { id: 2, name: '服装' },
  { id: 3, name: '食品' },
  { id: 4, name: '图书' }
]

const product = ref<any>(null)
const quantity = ref(1)
const categories = ref(mockCategories)

const categoryName = computed(() => {
  if (!product.value) return ''
  const cat = categories.value.find(c => c.id === product.value.categoryId)
  return cat?.name || ''
})

const loadProduct = (id: number) => {
  product.value = mockProducts.find(p => p.id === id) || mockProducts[0]
}

const decreaseQty = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const increaseQty = () => {
  if (product.value && quantity.value < product.value.stock) {
    quantity.value++
  }
}

const addToCart = () => {
  if (!product.value) {
    uni.showToast({ title: '商品信息未加载', icon: 'none' })
    return
  }
  
  try {
    let existingCart = uni.getStorageSync('cartItems')
    if (!existingCart) {
      existingCart = '[]'
    }
    
    let cartItems = []
    try {
      cartItems = JSON.parse(existingCart)
    } catch {
      cartItems = []
    }
    
    if (!Array.isArray(cartItems)) {
      cartItems = []
    }
    
    const existingItem = cartItems.find((item: any) => item.productId === product.value.id)
    if (existingItem) {
      existingItem.quantity += quantity.value
    } else {
      cartItems.push({
        productId: product.value.id,
        productName: product.value.name,
        price: product.value.price,
        quantity: quantity.value,
        image: product.value.image
      })
    }
    
    uni.setStorageSync('cartItems', JSON.stringify(cartItems))
    uni.showToast({ 
      title: `已加入购物车 (${quantity.value}件)`, 
      icon: 'success' 
    })
  } catch (e: any) {
    uni.showToast({ title: '加入失败: ' + (e.message || '未知错误'), icon: 'none' })
  }
}

const buyNow = () => {
  uni.setStorageSync('checkoutItems', JSON.stringify([{
    productId: product.value.id,
    productName: product.value.name,
    price: product.value.price,
    quantity: quantity.value
  }]))
  uni.navigateTo({ url: '/pages/checkout/checkout' })
}

const goToCart = () => {
  uni.switchTab({ url: '/pages/cart/cart' })
}

const goToOrders = () => {
  uni.switchTab({ url: '/pages/orders/orders' })
}

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const id = Number((currentPage as any).options?.id || 1)
  
  loadProduct(id)
})
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 140rpx;
}

.product-detail {
  background-color: #fff;
  margin-bottom: 20rpx;
}

.product-image {
  width: 100%;
  height: 600rpx;
}

.product-header {
  padding: 20rpx;
}

.product-name {
  display: block;
  font-size: 36rpx;
  color: #333;
  font-weight: bold;
}

.product-price {
  display: block;
  font-size: 44rpx;
  color: #ff6b6b;
  font-weight: bold;
  margin-top: 15rpx;
}

.product-desc {
  padding: 0 20rpx;
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.product-info {
  margin-top: 20rpx;
  padding: 20rpx;
  background-color: #f9f9f9;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 10rpx 0;
}

.info-label {
  font-size: 28rpx;
  color: #999;
}

.info-value {
  font-size: 28rpx;
  color: #333;
  
  &.status-active {
    color: #67c23a;
  }
  
  &.status-inactive {
    color: #f56c6c;
  }
}

.quantity-select {
  padding: 20rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.quantity-label {
  font-size: 30rpx;
  color: #333;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 30rpx;
}

.qty-btn {
  width: 60rpx;
  height: 60rpx;
  line-height: 60rpx;
  text-align: center;
  background-color: #f5f5f5;
  border-radius: 50%;
  font-size: 36rpx;
  color: #333;
}

.qty-value {
  font-size: 32rpx;
  color: #333;
  min-width: 60rpx;
  text-align: center;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 120rpx;
  background-color: #fff;
  display: flex;
  align-items: center;
  padding: 0 20rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0,0,0,0.1);
}

.bar-left {
  display: flex;
  gap: 40rpx;
}

.bar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.bar-icon {
  font-size: 40rpx;
}

.bar-text {
  font-size: 22rpx;
  color: #666;
  margin-top: 5rpx;
}

.bar-right {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
}

.btn-cart {
  width: 180rpx;
  height: 80rpx;
  line-height: 80rpx;
  text-align: center;
  background-color: #ffa502;
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
}

.btn-buy {
  width: 180rpx;
  height: 80rpx;
  line-height: 80rpx;
  text-align: center;
  background-color: #ff6b6b;
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
}
</style>