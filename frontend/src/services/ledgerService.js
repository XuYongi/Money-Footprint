import http from './http'

class LedgerService {
  // 获取所有账本
  getLedgers() {
    return http.get('/ledgers')
  }

  // 根据用户ID获取账本
  getLedgersByUserId(userId) {
    return http.get(`/ledgers/user/${userId}`)
  }

  // 获取账本详情
  getLedger(id) {
    return http.get(`/ledgers/${id}`)
  }

  // 创建账本
  createLedger(ledger) {
    return http.post('/ledgers', ledger)
  }

  // 更新账本
  updateLedger(id, ledger) {
    return http.put(`/ledgers/${id}`, ledger)
  }

  // 删除账本
  deleteLedger(id) {
    return http.delete(`/ledgers/${id}`)
  }

  // 添加成员到账本
  addMember(ledgerId, userId) {
    return http.post(`/ledgers/${ledgerId}/members`, { ledgerId, userId })
  }

  // 从账本移除成员
  removeMember(ledgerId, userId) {
    return http.delete(`/ledgers/${ledgerId}/members/${userId}`)
  }
}

export default new LedgerService()