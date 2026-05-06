<template>
  <div class="layout">
    <el-container>
      <el-aside width="200px" class="aside">
        <div class="logo">
          <h2>电商管理系统</h2>
        </div>
        <el-menu :default-active="activeMenu" class="menu" mode="vertical" @select="handleMenuSelect">
          <el-menu-item index="/dashboard">
            <el-icon><component :is="icons.Home" /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>
          <el-menu-item index="/product">
            <el-icon><component :is="icons.ShoppingCart" /></el-icon>
            <span>商品管理</span>
          </el-menu-item>
          <el-menu-item index="/category">
            <el-icon><component :is="icons.Folder" /></el-icon>
            <span>分类管理</span>
          </el-menu-item>
          <el-menu-item index="/order">
            <el-icon><component :is="icons.Document" /></el-icon>
            <span>订单管理</span>
          </el-menu-item>
          <el-menu-item index="/user">
            <el-icon><component :is="icons.User" /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="header-right">
            <el-dropdown>
              <span class="user-info">
                <el-icon><component :is="icons.User" /></el-icon>
                <span>{{ userInfo.nickname }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { HomeFilled, ShoppingCart, Folder, Document, User } from '@element-plus/icons-vue'
import { logout } from '../api'

const router = useRouter()
const icons = { Home: HomeFilled, ShoppingCart, Folder, Document, User }

const userInfo = ref({ nickname: '' })

const activeMenu = computed(() => {
  return router.currentRoute.value.path
})

const handleMenuSelect = (index) => {
  router.push(index)
}

const handleLogout = async () => {
  await logout()
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  localStorage.removeItem('userId')
  router.push('/login')
}

onMounted(() => {
  const info = localStorage.getItem('userInfo')
  if (info) {
    userInfo.value = JSON.parse(info)
  }
})
</script>

<style scoped>
.layout {
  height: 100vh;
}

.aside {
  background-color: #2f4050;
}

.logo {
  color: white;
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #1f2d3d;
}

.menu {
  border-right: none;
}

.header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 0 20px;
}

.main {
  padding: 20px;
}
</style>