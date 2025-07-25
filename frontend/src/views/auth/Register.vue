<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="register-header">
          <h2>用户注册</h2>
        </div>
      </template>
      <el-form 
        :model="registerForm" 
        :rules="registerRules" 
        ref="registerFormRef" 
        class="register-form"
        @submit.native.prevent="handleRegister"
      >
        <el-form-item label="用户名" prop="username">
          <el-input 
            v-model="registerForm.username" 
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
          />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input 
            v-model="registerForm.email" 
            placeholder="请输入邮箱"
            prefix-icon="el-icon-message"
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="registerForm.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="registerForm.confirmPassword" 
            type="password" 
            placeholder="请再次输入密码"
            prefix-icon="el-icon-lock"
            @keyup.enter="handleRegister"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleRegister" 
            :loading="loading"
            class="register-button"
            native-type="submit"
          >
            注册
          </el-button>
        </el-form-item>
        
        <div class="login-link">
          已有账号？<el-button type="text" @click="goToLogin">立即登录</el-button>
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

// 注册表单数据
const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

// 注册表单验证规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度应在3-20个字符之间', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 注册表单引用
const registerFormRef = ref()

// 处理注册
const handleRegister = () => {
  registerFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      
      // 构造用户对象
      const user = {
        username: registerForm.username,
        email: registerForm.email,
        plainPassword: registerForm.password
      }
      
      authService.register(user)
        .then(response => {
          // 保存令牌和用户信息到本地存储
          localStorage.setItem('token', response.data.token)
          localStorage.setItem('user', JSON.stringify(response.data.user))
          
          ElMessage.success('注册成功')
          // 跳转到首页
          router.push('/')
        })
        .catch(error => {
          ElMessage.error(error.response?.data || '注册失败')
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

// 跳转到登录页面
const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.register-card {
  width: 400px;
  max-width: 90%;
}

.register-header {
  text-align: center;
}

.register-form {
  padding: 20px 0;
}

.register-button {
  width: 100%;
}

.login-link {
  text-align: center;
  margin-top: 20px;
}
</style>