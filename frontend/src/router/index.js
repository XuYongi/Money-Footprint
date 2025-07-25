import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import BillList from '@/views/bill/BillList.vue'
import LedgerList from '@/views/ledger/LedgerList.vue'
import AccountTypeList from '@/views/accounttype/AccountTypeList.vue'
import Statistics from '@/views/statistics/Statistics.vue'
import Login from '@/views/auth/Login.vue'
import Register from '@/views/auth/Register.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/bills',
    name: 'BillList',
    component: BillList
  },
  {
    path: '/ledgers',
    name: 'LedgerList',
    component: LedgerList
  },
  {
    path: '/account-types',
    name: 'AccountTypeList',
    component: AccountTypeList
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: Statistics
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 添加路由守卫
router.beforeEach((to, from, next) => {
  const publicPages = ['/login', '/register']
  const authRequired = !publicPages.includes(to.path)
  const loggedIn = localStorage.getItem('token')

  // 尝试访问受保护页面但未登录
  if (authRequired && !loggedIn) {
    return next('/login')
  }

  // 已登录用户尝试访问登录/注册页面
  if (loggedIn && (to.path === '/login' || to.path === '/register')) {
    return next('/')
  }

  next()
})

export default router