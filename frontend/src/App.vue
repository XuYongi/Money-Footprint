<template>
  <div id="app">
    <el-container v-if="$route.name !== 'Login' && $route.name !== 'Register'">
      <el-header>
        <el-menu
          :default-active="$route.path"
          class="el-menu-demo"
          mode="horizontal"
          router
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
        >
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/bills">账单管理</el-menu-item>
          <el-menu-item index="/ledgers">账本管理</el-menu-item>
          <el-menu-item index="/account-types">账目类型</el-menu-item>
          <el-menu-item index="/statistics">统计分析</el-menu-item>
          <div class="navbar-right">
            <el-dropdown v-if="currentUser" @command="handleUserCommand">
              <span class="el-dropdown-link">
                {{ currentUser.username }}<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <div v-else>
              <el-button @click="$router.push('/login')" type="text" style="color: white;">登录</el-button>
              <el-button @click="$router.push('/register')" type="text" style="color: white;">注册</el-button>
            </div>
          </div>
        </el-menu>
      </el-header>
      
      <el-main>
        <router-view />
      </el-main>
    </el-container>
    
    <router-view v-else />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import authService from '@/services/authService'

const route = useRoute()
const router = useRouter()
const currentUser = ref(null)

// 获取当前用户信息
const getCurrentUser = () => {
  currentUser.value = authService.getCurrentUser()
}

// 处理用户命令
const handleUserCommand = (command) => {
  if (command === 'logout') {
    authService.logout()
    currentUser.value = null
    ElMessage.success('已退出登录')
    router.push('/login')
  }
}

// 监听路由变化
watch(() => route.name, () => {
  getCurrentUser()
})

// 组件挂载时获取用户信息
onMounted(() => {
  getCurrentUser()
})
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.el-header {
  padding: 0;
}

.navbar-right {
  float: right;
  height: 100%;
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #fff;
}

.el-icon-arrow-down {
  font-size: 12px;
}
</style>