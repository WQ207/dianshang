<template>
  <div class="client-layout">
    <el-header class="header">
      <div class="header-left">
        <h1 class="logo" @click="$router.push('/client/index')">电商商城</h1>
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索商品" 
          class="search-input"
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </div>
      <div class="header-right">
        <el-button @click="handleLogin">
          <el-icon><User /></el-icon>
          <span>登录</span>
        </el-button>
        <el-button @click="goToCart">
          <el-icon><ShoppingCart /></el-icon>
          <span>购物车</span>
          <el-badge v-if="cartCount > 0" :value="cartCount" class="cart-badge" />
        </el-button>
        <el-button @click="goToOrders">
          <el-icon><Document /></el-icon>
          <span>我的订单</span>
        </el-button>
        <el-button @click="goToAdmin">管理后台</el-button>
      </div>
    </el-header>
    <el-main class="main">
      <router-view />
    </el-main>
    <el-footer class="footer">
      <p>电商商城 © 2024 - 版权所有</p>
    </el-footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, ShoppingCart, Document } from '@element-plus/icons-vue'
import { getCartCount } from '../../api'

const router = useRouter()
const searchKeyword = ref('')
const cartCount = ref(0)

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/client/index', query: { keyword: searchKeyword.value } })
  }
}

const handleLogin = () => {
  router.push('/login')
}

const goToCart = () => {
  router.push('/client/cart')
}

const goToOrders = () => {
  router.push('/client/orders')
}

const goToAdmin = () => {
  router.push('/dashboard')
}

const loadCartCount = async () => {
  const userId = localStorage.getItem('userId') || 1
  const res = await getCartCount(userId)
  if (res.code === 200) {
    cartCount.value = res.data.count
  }
}

onMounted(() => {
  loadCartCount()
})
</script>

<style scoped>
.client-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  cursor: pointer;
}

.search-input {
  width: 300px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.cart-badge {
  margin-left: 4px;
}

.main {
  flex: 1;
  padding: 20px;
}

.footer {
  text-align: center;
  padding: 20px;
  background-color: #f5f5f5;
}
</style>