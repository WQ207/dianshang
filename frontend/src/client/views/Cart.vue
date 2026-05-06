<template>
  <div class="cart-page">
    <h2>我的购物车</h2>
    
    <div v-if="cartItems.length === 0" class="empty-cart">
      <el-empty description="购物车为空" />
      <el-button type="primary" @click="$router.push('/client/index')">去购物</el-button>
    </div>
    
    <div v-else>
      <el-table :data="cartItems" border class="cart-table">
        <el-table-column label="商品信息">
          <template #default="scope">
            <div class="product-info">
              <img :src="getProductImage(scope.row)" class="product-img" />
              <div class="product-detail">
                <div class="product-name">{{ scope.row.productName }}</div>
                <div class="product-price">¥{{ scope.row.price }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="数量">
          <template #default="scope">
            <div class="quantity-control">
              <el-button size="small" @click="decreaseQuantity(scope.row)">-</el-button>
              <span>{{ scope.row.quantity }}</span>
              <el-button size="small" @click="increaseQuantity(scope.row)">+</el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="小计">
          <template #default="scope">
            <span class="subtotal">¥{{ (scope.row.price * scope.row.quantity).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="cart-footer">
        <div class="footer-left">
          <el-button type="danger" @click="handleClear">清空购物车</el-button>
        </div>
        <div class="footer-right">
          <span class="total-label">合计：</span>
          <span class="total-price">¥{{ totalAmount.toFixed(2) }}</span>
          <el-button type="primary" size="large" @click="handleCheckout">去结算 ({{ totalCount }})</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCartItems, updateCartItem, deleteCartItem, clearCart } from '../../api'

const router = useRouter()
const cartItems = ref([])
const userId = ref(localStorage.getItem('userId') || 1)

const totalAmount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const totalCount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
})

const getProductImage = (item) => {
  return item.image || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=product%20photo%20white%20background&image_size=square'
}

const decreaseQuantity = async (item) => {
  if (item.quantity > 1) {
    item.quantity--
    await updateCartItem(userId.value, item.productId, item.quantity)
  }
}

const increaseQuantity = async (item) => {
  item.quantity++
  await updateCartItem(userId.value, item.productId, item.quantity)
}

const handleDelete = async (item) => {
  if (confirm('确定删除该商品？')) {
    await deleteCartItem(userId.value, item.productId)
    cartItems.value = cartItems.value.filter(i => i.id !== item.id)
  }
}

const handleClear = async () => {
  if (confirm('确定清空购物车？')) {
    await clearCart(userId.value)
    cartItems.value = []
  }
}

const handleCheckout = () => {
  sessionStorage.setItem('checkoutItems', JSON.stringify(cartItems.value))
  sessionStorage.setItem('checkoutUserId', userId.value)
  router.push('/client/checkout')
}

const loadCartItems = async () => {
  const res = await getCartItems(userId.value)
  if (res.code === 200) {
    cartItems.value = res.data
  }
}

onMounted(() => {
  loadCartItems()
})
</script>

<style scoped>
.cart-page {
  padding: 20px;
}

.cart-page h2 {
  margin-bottom: 20px;
}

.empty-cart {
  text-align: center;
  padding: 50px 0;
}

.empty-cart button {
  margin-top: 20px;
}

.cart-table {
  margin-bottom: 20px;
}

.product-info {
  display: flex;
  gap: 15px;
}

.product-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
}

.product-detail {
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 14px;
  margin-bottom: 5px;
}

.product-price {
  font-size: 16px;
  color: #F56C6C;
  font-weight: bold;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 10px;
}

.quantity-control button {
  width: 30px;
  height: 30px;
}

.subtotal {
  font-size: 18px;
  color: #F56C6C;
  font-weight: bold;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 8px;
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.total-label {
  font-size: 16px;
}

.total-price {
  font-size: 24px;
  color: #F56C6C;
  font-weight: bold;
}
</style>