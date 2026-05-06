<template>
  <div class="client-index">
    <div class="banner">
      <div class="banner-content">
        <h2>欢迎来到电商商城</h2>
        <p>品质保证 · 优质服务 · 快速配送</p>
      </div>
    </div>
    
    <div class="category-section">
      <h3>商品分类</h3>
      <div class="category-list">
        <el-button 
          v-for="cat in categories" 
          :key="cat.id"
          :class="{ active: selectedCategory === cat.id }"
          @click="handleCategoryChange(cat.id)"
        >
          {{ cat.name }}
        </el-button>
      </div>
    </div>
    
    <div class="product-section">
      <div class="section-header">
        <h3>热门商品</h3>
      </div>
      <div class="product-grid">
        <el-card 
          v-for="product in products" 
          :key="product.id" 
          class="product-card"
          @click="handleProductClick(product.id)"
        >
          <div class="product-image">
            <img :src="getProductImage(product)" :alt="product.name" />
          </div>
          <div class="product-info">
            <h4>{{ product.name }}</h4>
            <p class="description">{{ product.description }}</p>
            <div class="price">¥{{ product.price }}</div>
            <div class="stock">库存: {{ product.stock }}</div>
            <el-button type="primary" @click.stop="handleAddToCart(product)">加入购物车</el-button>
          </div>
        </el-card>
      </div>
      
      <el-pagination
        v-if="total > pageSize"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @current-change="handlePageChange"
        class="pagination"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProductPage, getCategoryList, addCartItem } from '../../api'

const route = useRoute()
const router = useRouter()
const products = ref([])
const categories = ref([])
const selectedCategory = ref(null)
const currentPage = ref(1)
const pageSize = ref(8)
const total = ref(0)

const getProductImage = (product) => {
  return product.image || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=product%20photo%20white%20background&image_size=square'
}

const handleProductClick = (productId) => {
  router.push(`/client/product/${productId}`)
}

const handleAddToCart = async (product) => {
  const userId = localStorage.getItem('userId') || 1
  const res = await addCartItem(userId, product.id, 1)
  if (res.code === 200) {
    alert('已添加到购物车')
  }
}

const handleCategoryChange = (categoryId) => {
  selectedCategory.value = categoryId
  currentPage.value = 1
  loadProducts()
}

const loadProducts = async () => {
  const params = {
    page: currentPage.value,
    size: pageSize.value
  }
  
  if (route.query.keyword) {
    params.name = route.query.keyword
  }
  
  const res = await getProductPage(params)
  if (res.code === 200) {
    products.value = res.data.records
    total.value = res.data.total
  }
}

const loadCategories = async () => {
  const res = await getCategoryList()
  if (res.code === 200) {
    categories.value = res.data
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadProducts()
}

onMounted(() => {
  loadCategories()
  loadProducts()
})
</script>

<style scoped>
.client-index {
  padding: 20px;
}

.banner {
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  color: white;
  padding: 40px;
  text-align: center;
  border-radius: 8px;
  margin-bottom: 30px;
}

.banner-content h2 {
  font-size: 32px;
  margin-bottom: 10px;
}

.banner-content p {
  font-size: 18px;
  opacity: 0.9;
}

.category-section {
  margin-bottom: 30px;
}

.category-section h3 {
  margin-bottom: 15px;
  font-size: 20px;
}

.category-list {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.category-list button {
  padding: 8px 20px;
  border-radius: 20px;
}

.category-list button.active {
  background-color: #409EFF;
  border-color: #409EFF;
}

.product-section {
  margin-bottom: 30px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  font-size: 20px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.product-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-image {
  height: 200px;
  overflow: hidden;
  margin-bottom: 15px;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 0 10px;
}

.product-info h4 {
  font-size: 16px;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.description {
  font-size: 12px;
  color: #666;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.price {
  font-size: 20px;
  color: #F56C6C;
  font-weight: bold;
  margin-bottom: 5px;
}

.stock {
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
}

.pagination {
  margin-top: 30px;
  text-align: center;
}
</style>