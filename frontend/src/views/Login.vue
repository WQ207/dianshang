<template>
  <div class="login-container">
    <div class="login-box">
      <h2>电商后台管理系统</h2>
      <el-form :model="form" ref="formRef" label-width="80px" class="login-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api'

const router = useRouter()
const formRef = ref(null)
const form = ref({
  username: '',
  password: ''
})

const handleLogin = async () => {
  try {
    const response = await login(form.value)
    if (response.code === 200) {
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('userInfo', JSON.stringify(response.data.userInfo))
      localStorage.setItem('userId', response.data.userInfo.id.toString())
      router.push('/dashboard')
    } else {
      alert(response.message)
    }
  } catch (error) {
    alert('登录失败')
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.login-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.login-form {
  padding: 0 20px;
}

.login-btn {
  width: 100%;
}
</style>