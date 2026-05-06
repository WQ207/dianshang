<template>
  <div class="product-page">
    <div class="page-header">
      <div class="search-box">
        <input v-model="searchName" placeholder="搜索商品名称" class="search-input" @keyup.enter="handleSearch" />
        <button class="search-btn" @click="handleSearch">搜索</button>
      </div>
      <button class="add-btn" @click="handleAdd">新增商品</button>
    </div>
    
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>商品名称</th>
          <th>价格</th>
          <th>库存</th>
          <th>分类</th>
          <th>状态</th>
          <th>创建时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in tableData" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.name }}</td>
          <td>{{ item.price }}</td>
          <td>{{ item.stock }}</td>
          <td>{{ item.categoryName }}</td>
          <td><span :class="item.status === 1 ? 'status-active' : 'status-inactive'">{{ item.status === 1 ? '上架' : '下架' }}</span></td>
          <td>{{ item.createTime }}</td>
          <td>
            <button class="edit-btn" @click="handleEdit(item)">编辑</button>
            <button class="delete-btn" @click="handleDelete(item)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal-content">
        <h3>{{ isEdit ? '编辑商品' : '新增商品' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-item">
            <label>商品名称</label>
            <input v-model="form.name" required />
          </div>
          <div class="form-item">
            <label>商品描述</label>
            <textarea v-model="form.description"></textarea>
          </div>
          <div class="form-item">
            <label>价格</label>
            <input v-model="form.price" type="number" step="0.01" required />
          </div>
          <div class="form-item">
            <label>库存</label>
            <input v-model="form.stock" type="number" required />
          </div>
          <div class="form-item">
            <label>分类</label>
            <select v-model="form.categoryId">
              <option value="">请选择分类</option>
              <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
          </div>
          <div class="form-item">
            <label>状态</label>
            <label class="checkbox-label">
              <input type="checkbox" v-model="form.status" :true-value="1" :false-value="0" />
              上架
            </label>
          </div>
          <div class="form-actions">
            <button type="button" class="cancel-btn" @click="showModal = false">取消</button>
            <button type="submit" class="submit-btn">确定</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProductPage, saveProduct, updateProduct, deleteProduct, getCategoryList } from '../api'

const searchName = ref('')
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showModal = ref(false)
const isEdit = ref(false)
const categories = ref([])

const form = ref({
  id: null,
  name: '',
  description: '',
  price: '',
  stock: 0,
  categoryId: '',
  status: 1
})

const loadData = async () => {
  const res = await getProductPage({ page: currentPage.value, size: pageSize.value, name: searchName.value })
  if (res.code === 200) {
    tableData.value = res.data.records.map(item => ({
      ...item,
      categoryName: categories.value.find(c => c.id === item.categoryId)?.name || ''
    }))
    total.value = res.data.total
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadData()
}

const handleAdd = () => {
  console.log('handleAdd called')
  isEdit.value = false
  form.value = {
    id: null,
    name: '',
    description: '',
    price: '',
    stock: 0,
    categoryId: '',
    status: 1
  }
  showModal.value = true
  console.log('showModal:', showModal.value)
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = {
    id: row.id,
    name: row.name,
    description: row.description,
    price: row.price.toString(),
    stock: row.stock,
    categoryId: row.categoryId?.toString() || '',
    status: row.status
  }
  showModal.value = true
}

const handleDelete = async (row) => {
  if (confirm('确定删除该商品？')) {
    const res = await deleteProduct(row.id)
    if (res.code === 200) {
      loadData()
    }
  }
}

const handleSubmit = async () => {
  const submitData = {
    ...form.value,
    price: parseFloat(form.value.price),
    stock: parseInt(form.value.stock),
    categoryId: form.value.categoryId ? parseInt(form.value.categoryId) : null,
    status: form.value.status ? 1 : 0
  }
  
  if (isEdit.value) {
    await updateProduct(submitData)
  } else {
    await saveProduct(submitData)
  }
  showModal.value = false
  loadData()
}

onMounted(async () => {
  const catRes = await getCategoryList()
  if (catRes.code === 200) {
    categories.value = catRes.data
  }
  loadData()
})
</script>

<style scoped>
.product-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-box {
  display: flex;
  gap: 10px;
}

.search-input {
  width: 300px;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.search-btn, .add-btn {
  padding: 8px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-btn {
  background-color: #67c23a;
  color: white;
}

.add-btn {
  background-color: #409eff;
  color: white;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.data-table th, .data-table td {
  border: 1px solid #e8eaec;
  padding: 12px;
  text-align: left;
}

.data-table th {
  background-color: #f5f7fa;
  font-weight: bold;
}

.status-active {
  color: #67c23a;
  font-weight: bold;
}

.status-inactive {
  color: #f56c6c;
  font-weight: bold;
}

.edit-btn, .delete-btn {
  padding: 4px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 8px;
}

.edit-btn {
  background-color: #67c23a;
  color: white;
}

.delete-btn {
  background-color: #f56c6c;
  color: white;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 24px;
  width: 480px;
  border-radius: 8px;
}

.modal-content h3 {
  margin-top: 0;
  margin-bottom: 20px;
}

.form-item {
  margin-bottom: 16px;
}

.form-item label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

.form-item input, .form-item textarea, .form-item select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-sizing: border-box;
}

.form-item textarea {
  height: 80px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: normal;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

.cancel-btn, .submit-btn {
  padding: 8px 24px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn {
  background-color: #f5f7fa;
  color: #606266;
}

.submit-btn {
  background-color: #409eff;
  color: white;
}
</style>