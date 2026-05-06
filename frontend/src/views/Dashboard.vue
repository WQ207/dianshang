<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon product-icon">
            <el-icon><component :is="icons.ShoppingCart" /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ stats.productCount }}</p>
            <p class="stat-label">商品数量</p>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon order-icon">
            <el-icon><component :is="icons.Document" /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ stats.orderCount }}</p>
            <p class="stat-label">订单数量</p>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon category-icon">
            <el-icon><component :is="icons.Folder" /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ stats.categoryCount }}</p>
            <p class="stat-label">分类数量</p>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon user-icon">
            <el-icon><component :is="icons.User" /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ stats.userCount }}</p>
            <p class="stat-label">用户数量</p>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <div class="chart-card">
          <h3>最近订单</h3>
          <el-table :data="recentOrders" border>
            <el-table-column prop="orderNo" label="订单号" />
            <el-table-column prop="totalAmount" label="金额" />
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" />
          </el-table>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <h3>热门商品</h3>
          <el-table :data="hotProducts" border>
            <el-table-column prop="name" label="商品名称" />
            <el-table-column prop="price" label="价格" />
            <el-table-column prop="stock" label="库存" />
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ShoppingCart, Document, Folder, User } from '@element-plus/icons-vue'
import { getProductList, getOrderPage } from '../api'

const icons = { ShoppingCart, Document, Folder, User }

const stats = ref({
  productCount: 0,
  orderCount: 0,
  categoryCount: 0,
  userCount: 1
})

const recentOrders = ref([])
const hotProducts = ref([])

const getStatusType = (status) => {
  const types = {
    0: 'warning',
    1: 'primary',
    2: 'success',
    3: 'success',
    4: 'danger'
  }
  return types[status] || 'default'
}

const getStatusText = (status) => {
  const texts = {
    0: '待付款',
    1: '已付款',
    2: '已发货',
    3: '已完成',
    4: '已取消'
  }
  return texts[status] || '未知'
}

onMounted(async () => {
  const productRes = await getProductList()
  if (productRes.code === 200) {
    stats.value.productCount = productRes.data.length
    hotProducts.value = productRes.data.slice(0, 5)
  }

  const orderRes = await getOrderPage({ page: 1, size: 5 })
  if (orderRes.code === 200) {
    stats.value.orderCount = orderRes.data.total
    recentOrders.value = orderRes.data.records
  }

  stats.value.categoryCount = 3
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 20px;
  font-size: 24px;
}

.product-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.order-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.category-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.user-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
}

.stat-label {
  color: #999;
  margin: 5px 0 0 0;
}

.chart-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.chart-card h3 {
  margin: 0 0 20px 0;
  font-size: 16px;
}
</style>