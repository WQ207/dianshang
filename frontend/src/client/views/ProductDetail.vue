<template>
  <div class="product-detail">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item @click="$router.push('/client/index')">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{ product.name }}</el-breadcrumb-item>
    </el-breadcrumb>
    
    <div class="product-content" v-if="product">
      <div class="product-image-section">
        <img :src="getProductImage(product)" :alt="product.name" class="main-image" />
      </div>
      
      <div class="product-info-section">
        <h1 class="product-name">{{ product.name }}</h1>
        <p class="product-description">{{ product.description }}</p>
        <div class="price-section">
          <span class="price">¥{{ product.price }}</span>
          <span class="stock">库存: {{ product.stock }}</span>
        </div>
        
        <div class="category-section">
          <span>分类：{{ categoryName }}</span>
        </div>
        
        <div class="quantity-section">
          <span>数量：</span>
          <el-button size="small" @click="decreaseQuantity">-</el-button>
          <span class="quantity">{{ quantity }}</span>
          <el-button size="small" @click="increaseQuantity">+</el-button>
        </div>
        
        <div class="action-section">
          <el-button type="primary" size="large" @click="handleAddToCart">加入购物车</el-button>
          <el-button type="success" size="large" @click="handleBuyNow">立即购买</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProduct, getCategoryList, addCartItem } from '../../api'

const route = useRoute()
const router = useRouter()
const product = ref(null)
const categories = ref([])
const quantity = ref(1)

const categoryName = computed(() => {
  if (!product.value) return ''
  const cat = categories.value.find(c => c.id === product.value.categoryId)
  return cat ? cat.name : ''
})

const getProductImage = (product) => {
  return product.image || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=product%20photo%20white%20background&image_size=landscape_4_3'
}

const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const increaseQuantity = () => {
  if (product.value && quantity.value < product.value.stock) {
    quantity.value++
  }
}

const handleAddToCart = async () => {
  const userId = localStorage.getItem('userId') || 1
  const res = await addCartItem(userId, product.value.id, quantity.value)
  if (res.code === 200) {
    alert('已添加到购物车')
  }
}

const handleBuyNow = () => {
  const userId = localStorage.getItem('userId') || 1
  const cartItems = [{
    productId: product.value.id,
    productName: product.value.name,
    price: product.value.price,
    quantity: quantity.value
  }]
  sessionStorage.setItem('checkoutItems', JSON.stringify(cartItems))
  sessionStorage.setItem('checkoutUserId', userId)
  router.push('/client/checkout')
}

const loadProduct = async () => {
  const productId = route.params.id
  const res = await getProduct(productId)
  if (res.code === 200) {
    product.value = res.data
  }
}

const loadCategories = async () => {
  const res = await getCategoryList()
  if (res.code === 200) {
    categories.value = res.data
  }
}

onMounted(() => {
  loadProduct()
  loadCategories()
})
</script>

<style scoped>
.product-detail {
  padding: 20px;
}

.breadcrumb {
  margin-bottom: 20px;
}

.product-content {
  display: flex;
  gap: 40px;
}

.product-image-section {
  flex: 1;
}

.main-image {
  width: 100%;
  max-width: 500px;
  height: 400px;
  object-fit: cover;
  border-radius: 8px;
}

.product-info-section {
  flex: 1;
}

.product-name {
  font-size: 28px;
  margin-bottom: 20px;
}

.product-description {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
  line-height: 1.6;
}

.price-section {
  margin-bottom: 20px;
}

.price {
  font-size: 32px;
  color: #F56C6C;
  font-weight: bold;
  margin-right: 20px;
}

.stock {
  font-size: 14px;
  color: #67C23A;
}

.category-section {
  font-size: 14px;
  color: #999;
  margin-bottom: 20px;
}

.quantity-section {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 30px;
}

.quantity-section button {
  width: 36px;
  height: 36px;
}

.quantity {
  font-size: 18px;
  min-width: 40px;
  text-align: center;
}

.action-section {
  display: flex;
  gap: 20px;
}

.action-section button {
  width: 150px;
}
</style>