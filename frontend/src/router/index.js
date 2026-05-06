import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: 'product',
        name: 'Product',
        component: () => import('../views/Product.vue')
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('../views/Category.vue')
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('../views/Order.vue')
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/User.vue')
      }
    ]
  },
  {
    path: '/client',
    name: 'ClientLayout',
    component: () => import('../client/layout/ClientLayout.vue'),
    redirect: '/client/index',
    children: [
      {
        path: 'index',
        name: 'ClientIndex',
        component: () => import('../client/views/Index.vue')
      },
      {
        path: 'product/:id',
        name: 'ClientProduct',
        component: () => import('../client/views/ProductDetail.vue')
      },
      {
        path: 'cart',
        name: 'ClientCart',
        component: () => import('../client/views/Cart.vue')
      },
      {
        path: 'checkout',
        name: 'ClientCheckout',
        component: () => import('../client/views/Checkout.vue')
      },
      {
        path: 'orders',
        name: 'ClientOrders',
        component: () => import('../client/views/Orders.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login' || to.path.startsWith('/client')) {
    next()
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router