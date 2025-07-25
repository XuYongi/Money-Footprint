import http from './http'

class AccountTypeService {
  // 获取所有账目类型
  getAccountTypes() {
    return http.get('/account-types')
  }

  // 根据用户ID获取账目类型
  getAccountTypesByUserId(userId) {
    return http.get(`/account-types/user/${userId}`)
  }

  // 根据用户ID和类别获取账目类型
  getAccountTypesByUserIdAndCategory(userId, category) {
    return http.get(`/account-types/user/${userId}/category/${category}`)
  }

  // 获取账目类型详情
  getAccountType(id) {
    return http.get(`/account-types/${id}`)
  }

  // 创建账目类型
  createAccountType(accountType) {
    return http.post('/account-types', accountType)
  }

  // 更新账目类型
  updateAccountType(id, accountType) {
    return http.put(`/account-types/${id}`, accountType)
  }

  // 删除账目类型
  deleteAccountType(id) {
    return http.delete(`/account-types/${id}`)
  }
}

export default new AccountTypeService()