const BASE_URL = 'http://127.0.0.1:8081/api'

interface ApiResponse {
  code: number
  message: string
  data?: any
}

export const api = {
  // 商品相关
  getProductPage: (params: Record<string, any>): Promise<ApiResponse> => {
    return request('/product/page', { params })
  },
  
  getProduct: (id: number): Promise<ApiResponse> => {
    return request(`/product/${id}`)
  },
  
  // 分类相关
  getCategoryList: (): Promise<ApiResponse> => {
    return request('/category/list')
  },
  
  // 订单相关
  getOrderPage: (params: Record<string, any>): Promise<ApiResponse> => {
    return request('/order/page', { params })
  },
  
  getOrder: (id: number): Promise<ApiResponse> => {
    return request(`/order/${id}`)
  },
  
  createOrder: (data: Record<string, any>): Promise<ApiResponse> => {
    return request('/order', { method: 'POST', data })
  },
  
  updateOrderStatus: (id: number, status: string): Promise<ApiResponse> => {
    return request(`/order/${id}/status`, { method: 'PUT', params: { status } })
  },
  
  // 购物车相关
  getCartItems: (userId: number): Promise<ApiResponse> => {
    return request('/cart/items', { params: { userId } })
  },
  
  addCartItem: (userId: number, productId: number, quantity: number): Promise<ApiResponse> => {
    return request('/cart/add', { method: 'POST', params: { userId, productId, quantity } })
  },
  
  updateCartItem: (userId: number, productId: number, quantity: number): Promise<ApiResponse> => {
    return request('/cart/update', { method: 'PUT', params: { userId, productId, quantity } })
  },
  
  deleteCartItem: (userId: number, productId: number): Promise<ApiResponse> => {
    return request('/cart/delete', { method: 'DELETE', params: { userId, productId } })
  },
  
  clearCart: (userId: number): Promise<ApiResponse> => {
    return request('/cart/clear', { method: 'DELETE', params: { userId } })
  },
  
  getCartCount: (userId: number): Promise<ApiResponse> => {
    return request('/cart/count', { params: { userId } })
  }
}

function request(
  url: string,
  options: {
    method?: string
    params?: Record<string, any>
    data?: Record<string, any>
  } = {}
): Promise<ApiResponse> {
  const { method = 'GET', params = {}, data = {} } = options
  
  let fullUrl = BASE_URL + url
  
  if (method === 'GET' && Object.keys(params).length > 0) {
    const queryArray: string[] = []
    for (const key in params) {
      if (params[key] !== undefined && params[key] !== null) {
        queryArray.push(`${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
      }
    }
    if (queryArray.length > 0) {
      fullUrl += '?' + queryArray.join('&')
    }
  }
  
  return new Promise((resolve, reject) => {
    uni.request({
      url: fullUrl,
      method: method as 'GET' | 'POST' | 'PUT' | 'DELETE',
      data: method !== 'GET' ? data : undefined,
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        resolve(res.data as ApiResponse)
      },
      fail: (err) => {
        reject(err)
      }
    })
  })
}