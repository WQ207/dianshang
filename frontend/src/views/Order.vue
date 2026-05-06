<template>
  <div class="order-page">
    <div class="page-header">
      <el-button type="primary" @click="handleRefresh">刷新</el-button>
    </div>
    <el-table :data="tableData" border>
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="orderNo" label="订单号" />
      <el-table-column prop="totalAmount" label="总金额" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="receiver" label="收货人" />
      <el-table-column prop="phone" label="联系电话" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="handleView(scope.row)">查看详情</el-button>
          <el-button size="small" type="primary" @click="handleUpdateStatus(scope.row)">修改状态</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      @current-change="handlePageChange"
    />

    <el-dialog title="订单详情" :visible.sync="detailVisible" width="600px">
      <div v-if="selectedOrder">
        <el-row :gutter="20">
          <el-col :span="12">
            <p><strong>订单号：</strong>{{ selectedOrder.orderNo }}</p>
            <p><strong>金额：</strong>{{ selectedOrder.totalAmount }}</p>
            <p><strong>状态：</strong><el-tag :type="getStatusType(selectedOrder.status)">{{ getStatusText(selectedOrder.status) }}</el-tag></p>
          </el-col>
          <el-col :span="12">
            <p><strong>收货人：</strong>{{ selectedOrder.receiver }}</p>
            <p><strong>电话：</strong>{{ selectedOrder.phone }}</p>
            <p><strong>地址：</strong>{{ selectedOrder.address }}</p>
          </el-col>
        </el-row>
        <h4>商品明细</h4>
        <el-table :data="selectedOrder.items" border>
          <el-table-column prop="productName" label="商品名称" />
          <el-table-column prop="price" label="单价" />
          <el-table-column prop="quantity" label="数量" />
          <el-table-column prop="totalPrice" label="小计" />
        </el-table>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog title="修改订单状态" :visible.sync="statusVisible" width="400px">
      <el-form :model="statusForm" label-width="80px">
        <el-form-item label="订单状态">
          <el-select v-model="statusForm.status">
            <el-option v-for="(label, value) in statusOptions" :key="value" :label="label" :value="Number(value)" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusVisible = false">取消</el-button>
        <el-button type="primary" @click="handleStatusSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getOrderPage, getOrder, updateOrderStatus, getOrderStatusOptions } from '../api'

const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const statusVisible = ref(false)
const selectedOrder = ref(null)
const statusOptions = ref({})

const statusForm = ref({
  status: 0
})

const getStatusType = (status) => {
  const types = {
    0: 'warning',
    1: 'primary',
    2: 'success',
    3: 'success',
    4: 'danger'
  }
  return types[status] || 'default'
}

const getStatusText = (status) => {
  const texts = {
    0: '待付款',
    1: '已付款',
    2: '已发货',
    3: '已完成',
    4: '已取消'
  }
  return texts[status] || '未知'
}

const loadData = async () => {
  const res = await getOrderPage({ page: currentPage.value, size: pageSize.value })
  if (res.code === 200) {
    tableData.value = res.data.records
    total.value = res.data.total
  }
}

const handleView = async (row) => {
  const res = await getOrder(row.id)
  if (res.code === 200) {
    selectedOrder.value = res.data
    detailVisible.value = true
  }
}

const handleUpdateStatus = (row) => {
  selectedOrder.value = row
  statusForm.value = { status: row.status }
  statusVisible.value = true
}

const handleStatusSubmit = async () => {
  await updateOrderStatus(selectedOrder.value.id, statusForm.value.status)
  statusVisible.value = false
  loadData()
}

const handleRefresh = () => {
  loadData()
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadData()
}

onMounted(async () => {
  const res = await getOrderStatusOptions()
  if (res.code === 200) {
    statusOptions.value = res.data
  }
  loadData()
})
</script>

<style scoped>
.order-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}
</style>