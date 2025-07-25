import http from './http'

class AuthService {
  // 用户注册
  register(user) {
    return http.post('/auth/register', user)
  }

  // 用户登录
  login(credentials) {
    return http.post('/auth/login', credentials)
  }

  // 用户登出
  logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  // 获取当前用户
  getCurrentUser() {
    const user = localStorage.getItem('user')
    return user ? JSON.parse(user) : null
  }

  // 获取令牌
  getToken() {
    return localStorage.getItem('token')
  }

  // 检查用户是否已认证
  isAuthenticated() {
    const token = this.getToken()
    return !!token
  }
}

export default new AuthService()