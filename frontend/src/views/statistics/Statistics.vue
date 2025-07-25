<template>
  <div class="statistics">
    <el-row :gutter="20" class="mb-10">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon income">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">总收入</div>
              <div class="stat-value">¥{{ formatAmount(overview.totalIncome) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon expense">
              <el-icon><Histogram /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">总支出</div>
              <div class="stat-value">¥{{ formatAmount(overview.totalExpense) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon balance">
              <el-icon><DataAnalysis /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">结余</div>
              <div class="stat-value">¥{{ formatAmount(overview.balance) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon bill">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">账单数</div>
              <div class="stat-value">{{ overview.billCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>月度收支趋势</span>
            </div>
          </template>
          <div ref="trendChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>支出分类占比</span>
            </div>
          </template>
          <div ref="pieChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="mt-10">
      <el-col :span="24">
        <el-card class="recent-bills-card">
          <template #header>
            <div class="card-header">
              <span>近期账单</span>
            </div>
          </template>
          <el-table :data="recentBills" style="width: 100%" v-loading="loading">
            <el-table-column prop="date" label="日期" width="120">
              <template #default="scope">
                {{ formatDate(scope.row.date) }}
              </template>
            </el-table-column>
            <el-table-column prop="accountTypeName" label="类型" width="100" />
            <el-table-column prop="remark" label="备注" />
            <el-table-column prop="amount" label="金额" width="120">
              <template #default="scope">
                <span :class="getAmountClass(scope.row)">
                  {{ formatBillAmount(scope.row) }}
                </span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { 
  TrendCharts, 
  Histogram, 
  DataAnalysis, 
  Document 
} from '@element-plus/icons-vue'
import statisticsService from '@/services/statisticsService'
import accountTypeService from '@/services/accountTypeService'

// 图表引用
const trendChartRef = ref()
const pieChartRef = ref()
let trendChart = null
let pieChart = null

// 数据
const overview = reactive({
  totalIncome: 0,
  totalExpense: 0,
  balance: 0,
  billCount: 0
})

const recentBills = ref([])
const monthlyStats = ref([])
const expenseStats = ref([])

// 状态
const loading = ref(false)

// 格式化金额
const formatAmount = (amount) => {
  if (!amount) return '0.00'
  return parseFloat(amount).toFixed(2)
}

// 格式化账单金额
const formatBillAmount = (bill) => {
  const amount = parseFloat(bill.amount).toFixed(2)
  return bill.accountTypeCategory === 1 ? `+${amount}` : `-${amount}`
}

// 获取金额样式类
const getAmountClass = (bill) => {
  return bill.accountTypeCategory === 1 ? 'income-text' : 'expense-text'
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.getFullYear() + '-' + 
         String(date.getMonth() + 1).padStart(2, '0') + '-' + 
         String(date.getDate()).padStart(2, '0')
}

// 加载统计概览
const loadOverview = async () => {
  try {
    const response = await statisticsService.getOverview(1) // 假设用户ID为1
    Object.assign(overview, response.data)
  } catch (error) {
    console.error('加载统计概览失败:', error)
  }
}

// 加载近期账单
const loadRecentBills = async () => {
  try {
    loading.value = true
    const response = await statisticsService.getRecentBills(1) // 假设用户ID为1
    recentBills.value = await enhanceBillsWithAccountType(response.data)
  } catch (error) {
    console.error('加载近期账单失败:', error)
  } finally {
    loading.value = false
  }
}

// 为账单添加账目类型信息
const enhanceBillsWithAccountType = async (bills) => {
  // 获取所有账目类型
  const accountTypesResponse = await accountTypeService.getAccountTypesByUserId(1)
  const accountTypes = accountTypesResponse.data
  
  // 为每个账单添加账目类型信息
  return bills.map(bill => {
    const accountType = accountTypes.find(type => type.id === bill.accountTypeId)
    return {
      ...bill,
      accountTypeName: accountType ? accountType.name : '未知类型',
      accountTypeCategory: accountType ? accountType.category : 0
    }
  })
}

// 加载月度统计
const loadMonthlyStatistics = async () => {
  try {
    const response = await statisticsService.getMonthlyStatistics(1) // 假设用户ID为1
    monthlyStats.value = response.data
    updateTrendChart()
  } catch (error) {
    console.error('加载月度统计失败:', error)
  }
}

// 加载支出分类统计
const loadExpenseByAccountType = async () => {
  try {
    const response = await statisticsService.getExpenseByAccountType(1) // 假设用户ID为1
    expenseStats.value = response.data
    updatePieChart()
  } catch (error) {
    console.error('加载支出分类统计失败:', error)
  }
}

// 更新趋势图
const updateTrendChart = () => {
  if (!trendChart || !monthlyStats.value) return
  
  // 处理月度统计数据
  const months = Object.keys(monthlyStats.value).sort()
  const incomeData = months.map(month => 
    monthlyStats.value[month].income ? parseFloat(monthlyStats.value[month].income) : 0)
  const expenseData = months.map(month => 
    monthlyStats.value[month].expense ? parseFloat(monthlyStats.value[month].expense) : 0)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['收入', '支出']
    },
    xAxis: {
      type: 'category',
      data: months
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '收入',
        type: 'line',
        data: incomeData,
        smooth: true,
        itemStyle: { color: '#67c23a' }
      },
      {
        name: '支出',
        type: 'line',
        data: expenseData,
        smooth: true,
        itemStyle: { color: '#f56c6c' }
      }
    ]
  }
  
  trendChart.setOption(option)
}

// 更新饼图
const updatePieChart = () => {
  if (!pieChart || !expenseStats.value) return
  
  // 处理支出分类数据
  const pieData = Object.keys(expenseStats.value).map(key => ({
    name: key,
    value: parseFloat(expenseStats.value[key])
  }))
  
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        type: 'pie',
        radius: '80%',
        data: pieData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  pieChart.setOption(option)
}

// 初始化图表
const initCharts = () => {
  // 初始化趋势图
  trendChart = echarts.init(trendChartRef.value)
  
  // 初始化饼图
  pieChart = echarts.init(pieChartRef.value)
  
  // 初始图表数据
  updateTrendChart()
  updatePieChart()
}

// 窗口大小调整时重绘图表
const resizeCharts = () => {
  if (trendChart) {
    trendChart.resize()
  }
  if (pieChart) {
    pieChart.resize()
  }
}

// 加载所有统计数据
const loadAllStatistics = async () => {
  await Promise.all([
    loadOverview(),
    loadRecentBills(),
    loadMonthlyStatistics(),
    loadExpenseByAccountType()
  ])
}

onMounted(() => {
  initCharts()
  loadAllStatistics()
  window.addEventListener('resize', resizeCharts)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeCharts)
  if (trendChart) {
    trendChart.dispose()
  }
  if (pieChart) {
    pieChart.dispose()
  }
})
</script>

<style scoped>
.mb-10 {
  margin-bottom: 10px;
}

.mt-10 {
  margin-top: 10px;
}

.stat-card {
  height: 120px;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
}

.stat-icon.income {
  background-color: #f0f9ff;
  color: #409eff;
}

.stat-icon.expense {
  background-color: #fef0f0;
  color: #f56c6c;
}

.stat-icon.balance {
  background-color: #f0f9ff;
  color: #67c23a;
}

.stat-icon.bill {
  background-color: #f0f9ff;
  color: #909399;
}

.stat-info {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
}

.chart-card {
  height: 400px;
}

.chart-container {
  height: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.income-text {
  color: #67c23a;
}

.expense-text {
  color: #f56c6c;
}

.recent-bills-card {
  height: 300px;
}
</style>