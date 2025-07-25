import http from './http'

class LedgerMemberService {
  // 获取账本的所有成员
  getMembersByLedgerId(ledgerId) {
    return http.get(`/ledger-members/ledger/${ledgerId}`)
  }

  // 根据用户ID获取成员关系
  getMembersByUserId(userId) {
    return http.get(`/ledger-members/user/${userId}`)
  }

  // 添加成员到账本
  addMember(ledgerMember) {
    return http.post('/ledger-members', ledgerMember)
  }

  // 从账本移除成员
  removeMember(ledgerId, userId) {
    return http.delete(`/ledger-members/${ledgerId}/${userId}`)
  }
}

export default new LedgerMemberService()