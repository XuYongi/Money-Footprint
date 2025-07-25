import http from './http'

class StatisticsService {
  // 获取用户统计概览
  getOverview(userId) {
    return http.get(`/statistics/overview/${userId}`)
  }

  // 获取用户最近的账单
  getRecentBills(userId) {
    return http.get(`/statistics/recent-bills/${userId}`)
  }

  // 获取用户月度统计
  getMonthlyStatistics(userId) {
    return http.get(`/statistics/monthly/${userId}`)
  }

  // 获取用户支出分类统计
  getExpenseByAccountType(userId) {
    return http.get(`/statistics/expense-by-type/${userId}`)
  }
}

export default new StatisticsService()