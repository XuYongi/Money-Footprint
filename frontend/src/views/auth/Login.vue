<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="login-header">
          <h2>用户登录</h2>
        </div>
      </template>
      <el-form 
        :model="loginForm" 
        :rules="loginRules" 
        ref="loginFormRef" 
        class="login-form"
        @submit.native.prevent="handleLogin"
      >
        <el-form-item label="用户名" prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleLogin" 
            :loading="loading"
            class="login-button"
            native-type="submit"
          >
            登录
          </el-button>
        </el-form-item>
        
        <div class="register-link">
          还没有账号？<el-button type="text" @click="goToRegister">立即注册</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import authService from '@/services/authService'

const router = useRouter()
const loading = ref(false)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 登录表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 登录表单引用
const loginFormRef = ref()

// 处理登录
const handleLogin = () => {
  loginFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      
      const credentials = {
        username: loginForm.username,
        password: loginForm.password
      }
      
      authService.login(credentials)
        .then(response => {
          // 保存令牌和用户信息到本地存储
          localStorage.setItem('token', response.data.token)
          localStorage.setItem('user', JSON.stringify(response.data.user))
          
          ElMessage.success('登录成功')
          // 跳转到首页
          router.push('/')
        })
        .catch(error => {
          ElMessage.error(error.response?.data || '登录失败')
        })
        .finally(() => {
          loading.value = false
        })
    } else {
      console.log('表单验证失败')
      return false
    }
  })
}

// 跳转到注册页面
const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.login-card {
  width: 400px;
  max-width: 90%;
}

.login-header {
  text-align: center;
}

.login-form {
  padding: 20px 0;
}

.login-button {
  width: 100%;
}

.register-link {
  text-align: center;
  margin-top: 20px;
}
</style>