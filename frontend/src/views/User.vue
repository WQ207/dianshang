<template>
  <div class="user-page">
    <div class="user-info-card">
      <div class="avatar-section">
        <el-avatar :size="120" icon="User" class="avatar" />
      </div>
      <div class="info-section">
        <h2>{{ userInfo.nickname }}</h2>
        <p><strong>用户名：</strong>{{ userInfo.username }}</p>
        <p><strong>邮箱：</strong>{{ userInfo.email }}</p>
        <p><strong>电话：</strong>{{ userInfo.phone }}</p>
        <p><strong>状态：</strong><el-tag :type="userInfo.status === 1 ? 'success' : 'danger'">{{ userInfo.status === 1 ? '启用' : '禁用' }}</el-tag></p>
      </div>
    </div>
    <el-card title="修改密码" class="password-card">
      <el-form :model="passwordForm" label-width="100px">
        <el-form-item label="原密码">
          <el-input v-model="passwordForm.oldPassword" type="password" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passwordForm.newPassword" type="password" />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="passwordForm.confirmPassword" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleChangePassword">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserInfo } from '../api'

const userInfo = ref({
  id: null,
  username: '',
  nickname: '',
  avatar: '',
  email: '',
  phone: '',
  status: 1
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const handleChangePassword = () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    alert('两次密码不一致')
    return
  }
  alert('密码修改成功')
  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
}

onMounted(async () => {
  const info = localStorage.getItem('userInfo')
  if (info) {
    userInfo.value = JSON.parse(info)
  }
})
</script>

<style scoped>
.user-page {
  padding: 20px;
}

.user-info-card {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.avatar-section {
  margin-right: 30px;
}

.avatar {
  border: 4px solid #667eea;
}

.info-section h2 {
  margin: 0 0 20px 0;
  color: #333;
}

.info-section p {
  margin: 10px 0;
}

.password-card {
  max-width: 500px;
}
</style>