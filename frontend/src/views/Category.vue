<template>
  <div class="category-page">
    <div class="page-header">
      <el-button type="primary" @click="handleAdd">新增分类</el-button>
    </div>
    <el-table :data="tableData" border>
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="sort" label="排序" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="分类信息" :visible.sync="dialogVisible" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="form.sort" type="number" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCategoryList, saveCategory, updateCategory, deleteCategory } from '../api'

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)

const form = ref({
  id: null,
  name: '',
  sort: 0,
  status: 1
})

const loadData = async () => {
  const res = await getCategoryList()
  if (res.code === 200) {
    tableData.value = res.data
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    id: null,
    name: '',
    sort: 0,
    status: 1
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = {
    id: row.id,
    name: row.name,
    sort: row.sort,
    status: row.status
  }
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  if (confirm('确定删除该分类？')) {
    const res = await deleteCategory(row.id)
    if (res.code === 200) {
      loadData()
    }
  }
}

const handleSubmit = async () => {
  const submitData = {
    ...form.value,
    sort: parseInt(form.value.sort),
    status: parseInt(form.value.status)
  }
  
  if (isEdit.value) {
    await updateCategory(submitData)
  } else {
    await saveCategory(submitData)
  }
  dialogVisible.value = false
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.category-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}
</style>