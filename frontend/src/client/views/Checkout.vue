<template>
  <div class="checkout-page">
    <h2>确认订单</h2>
    
    <div class="checkout-content">
      <div class="address-section">
        <h3>收货地址</h3>
        <el-form :model="addressForm" label-width="80px">
          <el-form-item label="收货人">
            <el-input v-model="addressForm.receiver" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="addressForm.phone" />
          </el-form-item>
          <el-form-item label="收货地址">
            <el-input type="textarea" v-model="addressForm.address" />
          </el-form-item>
        </el-form>
      </div>
      
      <div class="items-section">
        <h3>商品清单</h3>
        <el-table :data="cartItems" border>
          <el-table-column label="商品名称" prop="productName" />
          <el-table-column label="单价">
            <template #default="scope">¥{{ scope.row.price }}</template>
          </el-table-column>
          <el-table-column label="数量" prop="quantity" />
          <el-table-column label="小计">
            <template #default="scope">¥{{ (scope.row.price * scope.row.quantity).toFixed(2) }}</template>
          </el-table-column>
        </el-table>
      </div>
      
      <div class="summary-section">
        <div class="summary-row">
          <span>商品数量：</span>
          <span>{{ totalCount }} 件</span>
        </div>
        <div class="summary-row">
          <span>商品总额：</span>
          <span>¥{{ totalAmount.toFixed(2) }}</span>
        </div>
        <div class="summary-row total">
          <span>应付金额：</span>
          <span>¥{{ totalAmount.toFixed(2) }}</span>
        </div>
      </div>
    </div>
    
    <div class="checkout-footer">
      <div class="footer-info">
        <span>应付金额：</span>
        <span class="final-price">¥{{ totalAmount.toFixed(2) }}</span>
      </div>
      <el-button type="primary" size="large" @click="handleSubmitOrder">提交订单</el-button>
    </div>
    
    <el-dialog title="支付成功" :visible.sync="showSuccess" width="400px">
      <div class="success-content">
        <el-icon size="48" class="success-icon"><Check /></el-icon>
        <p>订单提交成功！</p>
        <p>订单号：{{ orderNo }}</p>
      </div>
      <template #footer>
        <el-button type="primary" @click="goToOrders">查看订单</el-button>
        <el-button @click="goToIndex">继续购物</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Check } from '@element-plus/icons-vue'
import { createOrder, clearCart } from '../../api'

const router = useRouter()
const cartItems = ref([])
const userId = ref(1)
const showSuccess = ref(false)
const orderNo = ref('')

const addressForm = ref({
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

const handleSubmitOrder = async () => {
  if (!addressForm.value.receiver || !addressForm.value.phone || !addressForm.value.address) {
    alert('请填写完整的收货信息')
    return
  }
  
  const orderData = {
    userId: userId.value,
    items: cartItems.value.map(item => ({
      productId: item.productId,
      productName: item.productName,
      price: item.price,
      quantity: item.quantity,
      totalPrice: item.price * item.quantity
    })),
    totalAmount: totalAmount.value,
    receiver: addressForm.value.receiver,
    phone: addressForm.value.phone,
    address: addressForm.value.address
  }
  
  const res = await createOrder(orderData)
  if (res.code === 200) {
    orderNo.value = res.data.orderNo
    await clearCart(userId.value)
    sessionStorage.removeItem('checkoutItems')
    showSuccess.value = true
  }
}

const goToOrders = () => {
  showSuccess.value = false
  router.push('/client/orders')
}

const goToIndex = () => {
  showSuccess.value = false
  router.push('/client/index')
}

onMounted(() => {
  const items = sessionStorage.getItem('checkoutItems')
  const uid = sessionStorage.getItem('checkoutUserId')
  if (items) {
    cartItems.value = JSON.parse(items)
  }
  if (uid) {
    userId.value = parseInt(uid)
  }
})
</script>

<style scoped>
.checkout-page {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.checkout-page h2 {
  margin-bottom: 30px;
}

.checkout-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.address-section, .items-section {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.address-section h3, .items-section h3 {
  margin-bottom: 20px;
  font-size: 18px;
}

.summary-section {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.summary-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.summary-row.total {
  font-size: 18px;
  font-weight: bold;
  color: #F56C6C;
  border-bottom: none;
}

.checkout-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background-color: #f5f5f5;
  margin-top: 30px;
  border-radius: 8px;
}

.footer-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.final-price {
  font-size: 28px;
  color: #F56C6C;
  font-weight: bold;
}

.success-content {
  text-align: center;
  padding: 30px;
}

.success-icon {
  color: #67C23A;
  margin-bottom: 20px;
}

.success-content p {
  margin-bottom: 10px;
}
</style>