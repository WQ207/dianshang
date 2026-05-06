<template>
  <view class="container">
    <view class="section">
      <text class="section-title">📍 收货地址</text>
      <view class="address-form">
        <view class="form-item">
          <text class="form-label">收货人</text>
          <input v-model="address.receiver" class="form-input" placeholder="请输入收货人姓名" />
        </view>
        <view class="form-item">
          <text class="form-label">联系电话</text>
          <input v-model="address.phone" class="form-input" placeholder="请输入联系电话" />
        </view>
        <view class="form-item">
          <text class="form-label">收货地址</text>
          <textarea v-model="address.address" class="form-textarea" placeholder="请输入详细地址" />
        </view>
      </view>
    </view>
    
    <view class="section">
      <text class="section-title">🛒 商品清单</text>
      <view class="product-list">
        <view v-for="item in cartItems" :key="item.productId" class="product-item">
          <image class="product-image" :src="item.image || '/static/default.png'" mode="aspectFill" />
          <view class="product-info">
            <text class="product-name">{{ item.productName }}</text>
            <text class="product-price">¥{{ item.price }}</text>
            <text class="product-qty">x{{ item.quantity }}</text>
          </view>
          <text class="product-subtotal">¥{{ (item.price * item.quantity).toFixed(2) }}</text>
        </view>
      </view>
    </view>
    
    <view class="section">
      <view class="summary-row">
        <text class="summary-label">商品数量</text>
        <text class="summary-value">{{ totalCount }} 件</text>
      </view>
      <view class="summary-row">
        <text class="summary-label">商品总额</text>
        <text class="summary-value">¥{{ totalAmount.toFixed(2) }}</text>
      </view>
      <view class="summary-row total">
        <text class="summary-label">应付金额</text>
        <text class="summary-value">¥{{ totalAmount.toFixed(2) }}</text>
      </view>
    </view>
    
    <view class="submit-btn" @click="submitOrder">提交订单</view>
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

const address = ref({
  receiver: '',
  phone: '',
  address: ''
})

const totalAmount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const totalCount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
})

const submitOrder = () => {
  if (!address.value.receiver || !address.value.phone || !address.value.address) {
    uni.showToast({ title: '请填写完整收货信息', icon: 'none' })
    return
  }
  
  const orderNo = 'ORD' + Date.now()
  
  const newOrder = {
    id: Date.now(),
    orderNo: orderNo,
    totalAmount: totalAmount.value,
    status: '待付款',
    items: cartItems.value.map(item => ({
      productId: item.productId,
      productName: item.productName,
      price: item.price,
      quantity: item.quantity,
      image: item.image
    })),
    createTime: new Date().toLocaleString(),
    receiver: address.value.receiver,
    phone: address.value.phone,
    address: address.value.address
  }
  
  const existingOrders = uni.getStorageSync('orders')
  let orders = existingOrders ? JSON.parse(existingOrders) : []
  orders.unshift(newOrder)
  uni.setStorageSync('orders', JSON.stringify(orders))
  
  uni.removeStorageSync('cartItems')
  
  uni.showModal({
    title: '订单提交成功',
    content: `订单号: ${orderNo}`,
    showCancel: false,
    success: () => {
      uni.switchTab({ url: '/pages/orders/orders' })
    }
  })
}

onMounted(() => {
  const items = uni.getStorageSync('checkoutItems')
  if (items) {
    cartItems.value = JSON.parse(items)
  }
})
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
  padding-bottom: 140rpx;
}

.section {
  background-color: #fff;
  border-radius: 15rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.section-title {
  display: block;
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.address-form {
  padding: 10rpx 0;
}

.form-item {
  margin-bottom: 20rpx;
}

.form-label {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 10rpx;
}

.form-input {
  width: 100%;
  height: 80rpx;
  padding: 0 20rpx;
  background-color: #f5f5f5;
  border-radius: 10rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.form-textarea {
  width: 100%;
  height: 150rpx;
  padding: 20rpx;
  background-color: #f5f5f5;
  border-radius: 10rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.product-list {
  padding: 10rpx 0;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 15rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
}

.product-image {
  width: 120rpx;
  height: 120rpx;
  border-radius: 10rpx;
}

.product-info {
  flex: 1;
  padding-left: 20rpx;
}

.product-name {
  display: block;
  font-size: 28rpx;
  color: #333;
}

.product-price {
  display: block;
  font-size: 26rpx;
  color: #ff6b6b;
  margin-top: 10rpx;
}

.product-qty {
  display: block;
  font-size: 24rpx;
  color: #999;
}

.product-subtotal {
  font-size: 28rpx;
  color: #ff6b6b;
  font-weight: bold;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  padding: 15rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
  
  &.total {
    border-bottom: none;
    padding-top: 20rpx;
    
    .summary-label {
      font-size: 30rpx;
      font-weight: bold;
    }
    
    .summary-value {
      font-size: 36rpx;
      color: #ff6b6b;
      font-weight: bold;
    }
  }
}

.summary-label {
  font-size: 28rpx;
  color: #666;
}

.summary-value {
  font-size: 28rpx;
  color: #333;
}

.submit-btn {
  position: fixed;
  bottom: 0;
  left: 20rpx;
  right: 20rpx;
  height: 100rpx;
  line-height: 100rpx;
  text-align: center;
  background-color: #ff6b6b;
  color: #fff;
  border-radius: 50rpx;
  font-size: 32rpx;
  font-weight: bold;
  box-shadow: 0 4rpx 20rpx rgba(255,107,107,0.4);
}
</style>