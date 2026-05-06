import axios from 'axios'

const instance = axios.create({
  baseURL: '/api',
  timeout: 5000
})

instance.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

instance.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const login = (data) => instance.post('/user/login', data)
export const logout = () => instance.post('/user/logout')
export const getUserInfo = () => instance.get('/user/info')

export const getProductPage = (params) => instance.get('/product/page', { params })
export const getProduct = (id) => instance.get(`/product/${id}`)
export const getProductList = () => instance.get('/product/list')
export const saveProduct = (data) => instance.post('/product', data)
export const updateProduct = (data) => instance.put('/product', data)
export const deleteProduct = (id) => instance.delete(`/product/${id}`)

export const getCategoryList = () => instance.get('/category/list')
export const getCategory = (id) => instance.get(`/category/${id}`)
export const saveCategory = (data) => instance.post('/category', data)
export const updateCategory = (data) => instance.put('/category', data)
export const deleteCategory = (id) => instance.delete(`/category/${id}`)

export const getOrderPage = (params) => instance.get('/order/page', { params })
export const getOrder = (id) => instance.get(`/order/${id}`)
export const updateOrderStatus = (id, status) => instance.put(`/order/${id}/status`, {}, { params: { status } })
export const getOrderStatusOptions = () => instance.get('/order/status/options')
export const createOrder = (data) => instance.post('/order', data)

export const getCartItems = (userId) => instance.get('/cart/items', { params: { userId } })
export const addCartItem = (userId, productId, quantity) => instance.post('/cart/add', {}, { params: { userId, productId, quantity } })
export const updateCartItem = (userId, productId, quantity) => instance.put('/cart/update', {}, { params: { userId, productId, quantity } })
export const deleteCartItem = (userId, productId) => instance.delete('/cart/delete', { params: { userId, productId } })
export const clearCart = (userId) => instance.delete('/cart/clear', { params: { userId } })
export const getCartCount = (userId) => instance.get('/cart/count', { params: { userId } })