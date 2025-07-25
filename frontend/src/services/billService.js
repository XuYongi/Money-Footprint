import http from './http'

class BillService {
  // 获取所有账单（分页）
  getBills(page = 0, size = 10) {
    return http.get(`/bills?page=${page}&size=${size}&sort=date,desc`)
  }

  // 根据用户ID获取账单（分页）
  getBillsByUserId(userId, page = 0, size = 10) {
    return http.get(`/bills/user/${userId}?page=${page}&size=${size}&sort=date,desc`)
  }

  // 根据账本ID获取账单（分页）
  getBillsByLedgerId(ledgerId, page = 0, size = 10) {
    return http.get(`/bills/ledger/${ledgerId}?page=${page}&size=${size}&sort=date,desc`)
  }

  // 根据账本ID和用户ID获取账单（分页）
  getBillsByLedgerIdAndUserId(ledgerId, userId, page = 0, size = 10) {
    return http.get(`/bills/ledger/${ledgerId}/user/${userId}?page=${page}&size=${size}&sort=date,desc`)
  }

  // 获取账单详情
  getBill(id) {
    return http.get(`/bills/${id}`)
  }

  // 创建账单
  createBill(bill) {
    return http.post('/bills', bill)
  }

  // 更新账单
  updateBill(id, bill) {
    return http.put(`/bills/${id}`, bill)
  }

  // 删除账单
  deleteBill(id) {
    return http.delete(`/bills/${id}`)
  }
}

export default new BillService()