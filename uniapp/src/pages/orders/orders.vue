<template>
  <view class="container">
    <view v-if="orders.length > 0" class="order-list">
      <view v-for="order in orders" :key="order.id" class="order-item">
        <view class="order-header">
          <text class="order-no">订单号: {{ order.orderNo }}</text>
          <text class="order-status" :class="getStatusClass(order.status)">
            {{ getStatusText(order.status) }}
          </text>
        </view>
        
        <view class="order-items">
          <view v-for="item in order.items" :key="item.productId" class="order-product">
            <image class="product-image" :src="item.image || '/static/default.png'" mode="aspectFill" />
            <view class="product-info">
              <text class="product-name">{{ item.productName }}</text>
              <text class="product-price">¥{{ item.price }}</text>
              <text class="product-qty">x{{ item.quantity }}</text>
            </view>
          </view>
        </view>
        
        <view class="order-footer">
          <text class="order-total">合计: ¥{{ order.totalAmount.toFixed(2) }}</text>
          <view class="order-actions">
            <view v-if="order.status === '待付款'" class="action-btn pay" @click="payOrder(order)">支付</view>
            <view v-if="order.status === '待发货'" class="action-btn cancel" @click="cancelOrder(order)">取消</view>
            <view v-if="order.status === '已发货'" class="action-btn confirm" @click="confirmOrder(order)">确认收货</view>
          </view>
        </view>
      </view>
    </view>
    
    <view v-else class="empty-order">
      <text class="empty-icon">📦</text>
      <text class="empty-text">暂无订单</text>
      <view class="empty-btn" @click="goToIndex">去购物</view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface OrderItem {
  productId: number
  productName: string
  price: number
  quantity: number
  image?: string
}

interface Order {
  id: number
  orderNo: string
  totalAmount: number
  status: string
  items: OrderItem[]
  createTime: string
}

const orders = ref<Order[]>([])

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    '待付款': '待付款',
    '已付款': '待发货',
    '已发货': '待收货',
    '已完成': '已完成',
    '已取消': '已取消'
  }
  return statusMap[status] || status
}

const getStatusClass = (status: string) => {
  const classMap: Record<string, string> = {
    '待付款': 'status-pending',
    '已付款': 'status-paid',
    '已发货': 'status-shipped',
    '已完成': 'status-completed',
    '已取消': 'status-canceled'
  }
  return classMap[status] || ''
}

const loadOrders = () => {
  const stored = uni.getStorageSync('orders')
  if (stored) {
    try {
      orders.value = JSON.parse(stored)
    } catch (e) {
      orders.value = []
    }
  }
}

const saveOrders = () => {
  uni.setStorageSync('orders', JSON.stringify(orders.value))
}

const payOrder = (order: Order) => {
  order.status = '已付款'
  saveOrders()
  uni.showToast({ title: '支付成功', icon: 'success' })
}

const cancelOrder = (order: Order) => {
  order.status = '已取消'
  saveOrders()
  uni.showToast({ title: '订单已取消', icon: 'success' })
}

const confirmOrder = (order: Order) => {
  order.status = '已完成'
  saveOrders()
  uni.showToast({ title: '已确认收货', icon: 'success' })
}

const goToIndex = () => {
  uni.switchTab({ url: '/pages/index/index' })
}

onMounted(() => {
  loadOrders()
})
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.order-list {
  padding: 20rpx;
}

.order-item {
  background-color: #fff;
  border-radius: 15rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.order-no {
  font-size: 26rpx;
  color: #666;
}

.order-status {
  font-size: 26rpx;
  font-weight: bold;
  
  &.status-pending {
    color: #ffa502;
  }
  
  &.status-paid {
    color: #409eff;
  }
  
  &.status-shipped {
    color: #67c23a;
  }
  
  &.status-completed {
    color: #999;
  }
  
  &.status-canceled {
    color: #999;
  }
}

.order-items {
  padding: 20rpx;
}

.order-product {
  display: flex;
  margin-bottom: 15rpx;
  
  &:last-child {
    margin-bottom: 0;
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
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.product-name {
  font-size: 28rpx;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  font-size: 26rpx;
  color: #ff6b6b;
  margin-top: 10rpx;
}

.product-qty {
  font-size: 24rpx;
  color: #999;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  background-color: #fafafa;
}

.order-total {
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
}

.order-actions {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  padding: 15rpx 30rpx;
  border-radius: 30rpx;
  font-size: 26rpx;
  
  &.pay {
    background-color: #ff6b6b;
    color: #fff;
  }
  
  &.cancel {
    background-color: #f5f5f5;
    color: #666;
  }
  
  &.confirm {
    background-color: #67c23a;
    color: #fff;
  }
}

.empty-order {
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
</style>