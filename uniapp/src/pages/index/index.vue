<template>
  <view class="container">
    <!-- 顶部搜索栏 -->
    <view class="search-bar">
      <input 
        v-model="searchKeyword" 
        class="search-input" 
        placeholder="搜索商品" 
        @confirm="handleSearch"
      />
      <view class="search-btn" @click="handleSearch">搜索</view>
    </view>

    <!-- 分类导航 -->
    <scroll-view class="category-scroll" scroll-x>
      <view 
        v-for="cat in categories" 
        :key="cat.id" 
        class="category-item"
        :class="{ active: currentCategory === cat.id }"
        @click="selectCategory(cat.id)"
      >
        {{ cat.name }}
      </view>
    </scroll-view>

    <!-- 商品列表 -->
    <view class="product-grid">
      <view 
        v-for="product in products" 
        :key="product.id" 
        class="product-item"
        @click="goToDetail(product.id)"
      >
        <image class="product-image" :src="product.image || '/static/default.png'" mode="aspectFill" />
        <view class="product-info">
          <text class="product-name">{{ product.name }}</text>
          <text class="product-price">¥{{ product.price }}</text>
          <text class="product-stock">库存: {{ product.stock }}</text>
        </view>
      </view>
    </view>

    <!-- 底部导航 -->
    <view class="tab-bar">
      <view class="tab-item active">
        <text class="tab-icon">🏠</text>
        <text class="tab-text">首页</text>
      </view>
      <view class="tab-item" @click="goToCart">
        <text class="tab-icon">🛒</text>
        <text class="tab-text">购物车</text>
        <view v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</view>
      </view>
      <view class="tab-item" @click="goToOrders">
        <text class="tab-icon">📋</text>
        <text class="tab-text">订单</text>
      </view>
      <view class="tab-item" @click="goToUser">
        <text class="tab-icon">👤</text>
        <text class="tab-text">我的</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'

const searchKeyword = ref('')
const currentCategory = ref(0)
const cartCount = ref(0)

const mockCategories = [
  { id: 0, name: '全部' },
  { id: 1, name: '电子产品' },
  { id: 2, name: '服装' },
  { id: 3, name: '食品' },
  { id: 4, name: '图书' }
]

const mockProducts = [
  { id: 1, name: 'iPhone 15', price: 5999, stock: 100, categoryId: 1, image: '' },
  { id: 2, name: 'MacBook Pro', price: 14999, stock: 50, categoryId: 1, image: '' },
  { id: 3, name: 'AirPods Pro', price: 1899, stock: 200, categoryId: 1, image: '' },
  { id: 4, name: '男士纯棉T恤', price: 99, stock: 500, categoryId: 2, image: '' },
  { id: 5, name: '运动跑鞋', price: 299, stock: 300, categoryId: 2, image: '' },
  { id: 6, name: '休闲牛仔裤', price: 199, stock: 400, categoryId: 2, image: '' },
  { id: 7, name: '进口巧克力', price: 68, stock: 150, categoryId: 3, image: '' },
  { id: 8, name: '坚果礼盒', price: 128, stock: 80, categoryId: 3, image: '' },
  { id: 9, name: 'Java编程思想', price: 89, stock: 200, categoryId: 4, image: '' },
  { id: 10, name: 'Vue3实战', price: 79, stock: 150, categoryId: 4, image: '' }
]

const categories = ref(mockCategories)

const filteredProducts = computed(() => {
  let result = [...mockProducts]
  
  if (currentCategory.value > 0) {
    result = result.filter(p => p.categoryId === currentCategory.value)
  }
  
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(p => p.name.toLowerCase().includes(keyword))
  }
  
  return result
})

const products = computed(() => filteredProducts.value)

const handleSearch = () => {
  currentCategory.value = 0
}

const selectCategory = (id: number) => {
  currentCategory.value = id
}

const goToDetail = (id: number) => {
  uni.navigateTo({ url: `/pages/detail/detail?id=${id}` })
}

const goToCart = () => {
  uni.switchTab({ url: '/pages/cart/cart' })
}

const goToOrders = () => {
  uni.switchTab({ url: '/pages/orders/orders' })
}

const goToUser = () => {
  uni.switchTab({ url: '/pages/user/user' })
}

onMounted(() => {
})
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 100rpx;
}

.search-bar {
  display: flex;
  padding: 20rpx;
  background-color: #fff;
  gap: 20rpx;
}

.search-input {
  flex: 1;
  height: 70rpx;
  padding: 0 20rpx;
  background-color: #f5f5f5;
  border-radius: 35rpx;
  font-size: 28rpx;
}

.search-btn {
  width: 120rpx;
  height: 70rpx;
  line-height: 70rpx;
  text-align: center;
  background-color: #ff6b6b;
  color: #fff;
  border-radius: 35rpx;
  font-size: 28rpx;
}

.category-scroll {
  white-space: nowrap;
  background-color: #fff;
  padding: 20rpx 0;
}

.category-item {
  display: inline-block;
  padding: 15rpx 30rpx;
  margin: 0 15rpx;
  background-color: #f5f5f5;
  border-radius: 30rpx;
  font-size: 28rpx;
  
  &.active {
    background-color: #ff6b6b;
    color: #fff;
  }
}

.product-grid {
  display: flex;
  flex-wrap: wrap;
  padding: 20rpx;
  gap: 20rpx;
}

.product-item {
  width: calc(50% - 10rpx);
  background-color: #fff;
  border-radius: 15rpx;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 300rpx;
}

.product-info {
  padding: 15rpx;
}

.product-name {
  display: block;
  font-size: 28rpx;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  display: block;
  font-size: 32rpx;
  color: #ff6b6b;
  font-weight: bold;
  margin-top: 10rpx;
}

.product-stock {
  display: block;
  font-size: 22rpx;
  color: #999;
  margin-top: 5rpx;
}

.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background-color: #fff;
  display: flex;
  box-shadow: 0 -2rpx 10rpx rgba(0,0,0,0.1);
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  
  &.active {
    .tab-text {
      color: #ff6b6b;
    }
  }
}

.tab-icon {
  font-size: 40rpx;
}

.tab-text {
  font-size: 22rpx;
  color: #666;
  margin-top: 5rpx;
}

.cart-badge {
  position: absolute;
  top: 5rpx;
  right: 30rpx;
  min-width: 32rpx;
  height: 32rpx;
  line-height: 32rpx;
  text-align: center;
  background-color: #ff6b6b;
  color: #fff;
  border-radius: 16rpx;
  font-size: 20rpx;
  padding: 0 8rpx;
}
</style>