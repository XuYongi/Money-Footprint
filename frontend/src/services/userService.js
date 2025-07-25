import http from './http'

class UserService {
  // 获取所有用户
  getUsers() {
    return http.get('/users')
  }

  // 获取用户详情
  getUser(id) {
    return http.get(`/users/${id}`)
  }

  // 创建用户
  createUser(user) {
    return http.post('/users', user)
  }

  // 更新用户
  updateUser(id, user) {
    return http.put(`/users/${id}`, user)
  }

  // 删除用户
  deleteUser(id) {
    return http.delete(`/users/${id}`)
  }
}

export default new UserService()