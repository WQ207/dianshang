<template>
  <div class="orders-page">
    <h2>我的订单</h2>
    
    <div class="order-tabs">
      <el-button 
        v-for="tab in tabs" 
        :key="tab.value"
        :class="{ active: activeTab === tab.value }"
        @click="activeTab = tab.value"
      >
        {{ tab.label }}
      </el-button>
    </div>
    
    <div v-if="orders.length === 0" class="empty-orders">
      <el-empty description="暂无订单" />
    </div>
    
    <div v-else class="order-list">
      <el-card v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <span :class="['order-status', order.status]">{{ getStatusText(order.status) }}</span>
        </div>
        
        <div class="order-items">
          <div v-for="item in order.items" :key="item.id" class="order-item">
            <img :src="getProductImage(item)" class="item-image" />
            <div class="item-info">
              <div class="item-name">{{ item.productName }}</div>
              <div class="item-price">¥{{ item.price }} x {{ item.quantity }}</div>
            </div>
            <div class="item-total">¥{{ item.totalPrice }}</div>
          </div>
        </div>
        
        <div class="order-footer">
          <span class="order-total">合计：¥{{ order.totalAmount }}</span>
          <div class="order-actions">
            <el-button 
              v-if="order.status === 0" 
              type="primary" 
              @click="handlePay(order)"
            >
              去支付
            </el-button>
            <el-button 
              v-if="order.status === 1" 
              type="success" 
              @click="handleReceive(order)"
            >
              确认收货
            </el-button>
            <el-button 
              v-if="order.status === 2" 
              type="primary" 
              @click="handleReceive(order)"
            >
              确认收货
            </el-button>
            <el-button v-if="order.status === 3" type="info">已完成</el-button>
            <el-button 
              v-if="order.status === 0" 
              type="danger" 
              @click="handleCancel(order)"
            >
              取消订单
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getOrderPage, getOrder, updateOrderStatus } from '../../api'

const activeTab = ref('all')
const orders = ref([])

const tabs = [
  { label: '全部', value: 'all' },
  { label: '待付款', value: '0' },
  { label: '待发货', value: '1' },
  { label: '待收货', value: '2' },
  { label: '已完成', value: '3' },
  { label: '已取消', value: '4' }
]

const filteredOrders = computed(() => {
  if (activeTab.value === 'all') {
    return orders.value
  }
  return orders.value.filter(o => o.status === parseInt(activeTab.value))
})

const getStatusText = (status) => {
  const texts = {
    0: '待付款',
    1: '待发货',
    2: '待收货',
    3: '已完成',
    4: '已取消'
  }
  return texts[status] || '未知'
}

const getProductImage = (item) => {
  return item.productImage || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=product%20photo%20white%20background&image_size=square'
}

const handlePay = async (order) => {
  if (confirm('确认支付？')) {
    await updateOrderStatus(order.id, 1)
    order.status = 1
  }
}

const handleReceive = async (order) => {
  if (confirm('确认收货？')) {
    await updateOrderStatus(order.id, 3)
    order.status = 3
  }
}

const handleCancel = async (order) => {
  if (confirm('确定取消订单？')) {
    await updateOrderStatus(order.id, 4)
    order.status = 4
  }
}

const loadOrders = async () => {
  const userId = localStorage.getItem('userId') || 1
  const res = await getOrderPage({ page: 1, size: 10 })
  if (res.code === 200) {
    orders.value = res.data.records
    orders.value.forEach(order => {
      order.items = []
    })
    for (let order of orders.value) {
      const orderRes = await getOrder(order.id)
      if (orderRes.code === 200) {
        order.items = orderRes.data.items || []
      }
    }
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders-page {
  padding: 20px;
}

.orders-page h2 {
  margin-bottom: 20px;
}

.order-tabs {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.order-tabs button.active {
  background-color: #409EFF;
  border-color: #409EFF;
}

.empty-orders {
  text-align: center;
  padding: 50px 0;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  border-radius: 8px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.order-no {
  font-size: 14px;
  color: #666;
}

.order-status {
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
}

.order-status.0 {
  background-color: #fef08a;
  color: #854d0e;
}

.order-status.1 {
  background-color: #dbeafe;
  color: #1d4ed8;
}

.order-status.2 {
  background-color: #dcfce7;
  color: #166534;
}

.order-status.3 {
  background-color: #f3f4f6;
  color: #6b7280;
}

.order-status.4 {
  background-color: #fee2e2;
  color: #991b1b;
}

.order-items {
  padding: 15px 0;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
}

.order-item:last-child {
  margin-bottom: 0;
}

.item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 14px;
  margin-bottom: 5px;
}

.item-price {
  font-size: 12px;
  color: #666;
}

.item-total {
  font-size: 16px;
  color: #F56C6C;
  font-weight: bold;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.order-total {
  font-size: 16px;
  font-weight: bold;
  color: #F56C6C;
}

.order-actions {
  display: flex;
  gap: 10px;
}
</style>