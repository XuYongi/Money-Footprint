<template>
  <div class="bill-list">
    <el-row :gutter="20">
      <el-col :span="18">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>账单列表</span>
              <el-button type="primary" @click="showAddForm">新增账单</el-button>
            </div>
          </template>
          
          <el-table :data="bills" style="width: 100%" v-loading="loading">
            <el-table-column prop="date" label="日期" width="120">
              <template #default="scope">
                {{ formatDate(scope.row.date) }}
              </template>
            </el-table-column>
            <el-table-column prop="accountTypeName" label="账目类型" width="120" />
            <el-table-column prop="amount" label="金额" width="120">
              <template #default="scope">
                <span :class="getAmountClass(scope.row)">
                  {{ formatAmount(scope.row) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="ledgerName" label="账本" width="120" />
            <el-table-column prop="remark" label="备注" />
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="mini" @click="showEditForm(scope.row)">编辑</el-button>
                <el-button size="mini" type="danger" @click="deleteBill(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 20, 50]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            style="margin-top: 20px; text-align: right;"
          />
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>{{ isEdit ? '编辑账单' : '新增账单' }}</span>
            </div>
          </template>
          
          <el-form :model="currentBill" :rules="rules" ref="billForm" label-width="80px">
            <el-form-item label="日期" prop="date">
              <el-date-picker
                v-model="currentBill.date"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
            
            <el-form-item label="账目类型" prop="accountTypeId">
              <el-select v-model="currentBill.accountTypeId" placeholder="请选择账目类型" style="width: 100%">
                <el-option
                  v-for="type in accountTypes"
                  :key="type.id"
                  :label="type.name"
                  :value="type.id"
                />
              </el-select>
            </el-form-item>
            
            <el-form-item label="金额" prop="amount">
              <el-input v-model.number="currentBill.amount" placeholder="请输入金额" />
            </el-form-item>
            
            <el-form-item label="账本" prop="ledgerId">
              <el-select v-model="currentBill.ledgerId" placeholder="请选择账本" style="width: 100%">
                <el-option
                  v-for="ledger in ledgers"
                  :key="ledger.id"
                  :label="ledger.name"
                  :value="ledger.id"
                />
              </el-select>
            </el-form-item>
            
            <el-form-item label="备注" prop="remark">
              <el-input v-model="currentBill.remark" type="textarea" />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="saveBill">保存</el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import billService from '@/services/billService'
import accountTypeService from '@/services/accountTypeService'
import ledgerService from '@/services/ledgerService'
import authService from '@/services/authService'

// 数据
const bills = ref([])
const accountTypes = ref([])
const ledgers = ref([])
const loading = ref(false)
const isEdit = ref(false)

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 当前用户
const currentUser = ref(null)

// 当前账单
const currentBill = reactive({
  id: null,
  userId: null,
  date: '',
  accountTypeId: null,
  amount: null,
  ledgerId: null,
  remark: ''
})

// 表单验证规则
const rules = {
  date: [
    { required: true, message: '请选择日期', trigger: 'change' }
  ],
  accountTypeId: [
    { required: true, message: '请选择账目类型', trigger: 'change' }
  ],
  amount: [
    { required: true, message: '请输入金额', trigger: 'blur' },
    { type: 'number', message: '金额必须为数字', trigger: 'blur' }
  ],
  ledgerId: [
    { required: true, message: '请选择账本', trigger: 'change' }
  ]
}

// 表单引用
const billFormRef = ref(null)

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.getFullYear() + '-' + 
         String(date.getMonth() + 1).padStart(2, '0') + '-' + 
         String(date.getDate()).padStart(2, '0')
}

// 获取金额样式类
const getAmountClass = (bill) => {
  const accountType = accountTypes.value.find(type => type.id === bill.accountTypeId)
  return accountType && accountType.category === 1 ? 'income-text' : 'expense-text'
}

// 格式化金额
const formatAmount = (bill) => {
  if (!bill.amount) return '0.00'
  const amount = parseFloat(bill.amount).toFixed(2)
  const accountType = accountTypes.value.find(type => type.id === bill.accountTypeId)
  if (accountType && accountType.category === 1) {
    return `+${amount}`
  } else {
    return `-${amount}`
  }
}

// 加载账单列表
const loadBills = async () => {
  try {
    loading.value = true
    const response = await billService.getBillsByUserId(currentUser.value.id, currentPage.value - 1, pageSize.value)
    // 检查响应数据是否存在并且有content属性
    if (response && response.data) {
      if (response.data.content) {
        // 分页数据
        bills.value = await enhanceBillsWithDetails(response.data.content)
        total.value = response.data.totalElements || 0
      } else {
        // 非分页数据
        bills.value = await enhanceBillsWithDetails(response.data)
        total.value = response.data.length || 0
      }
    } else {
      bills.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('加载账单列表失败:', error)
    ElMessage.error('加载账单列表失败: ' + (error.message || '未知错误'))
    bills.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 为账单添加详细信息
const enhanceBillsWithDetails = async (billsData) => {
  // 检查billsData是否有效
  if (!billsData || !Array.isArray(billsData)) {
    return []
  }
  
  // 确保账目类型和账本数据已加载
  if (accountTypes.value.length === 0) {
    await loadAccountTypes()
  }
  
  if (ledgers.value.length === 0) {
    await loadLedgers()
  }
  
  // 为每个账单添加详细信息
  return billsData.map(bill => {
    const accountType = accountTypes.value.find(type => type.id === bill.accountTypeId)
    const ledger = ledgers.value.find(l => l.id === bill.ledgerId)
    return {
      ...bill,
      accountTypeName: accountType ? accountType.name : '未知类型',
      ledgerName: ledger ? ledger.name : '未知账本'
    }
  })
}

// 加载账目类型
const loadAccountTypes = async () => {
  try {
    const response = await accountTypeService.getAccountTypesByUserId(currentUser.value.id)
    // 检查响应数据
    if (response && response.data) {
      accountTypes.value = Array.isArray(response.data) ? response.data : []
    } else {
      accountTypes.value = []
    }
  } catch (error) {
    console.error('加载账目类型失败:', error)
    ElMessage.error('加载账目类型失败: ' + (error.message || '未知错误'))
    accountTypes.value = []
  }
}

// 加载账本
const loadLedgers = async () => {
  try {
    const response = await ledgerService.getLedgersByUserId(currentUser.value.id)
    // 检查响应数据
    if (response && response.data) {
      ledgers.value = Array.isArray(response.data) ? response.data : []
    } else {
      ledgers.value = []
    }
  } catch (error) {
    console.error('加载账本失败:', error)
    ElMessage.error('加载账本失败: ' + (error.message || '未知错误'))
    ledgers.value = []
  }
}

// 显示新增表单
const showAddForm = () => {
  isEdit.value = false
  resetForm()
}

// 显示编辑表单
const showEditForm = (bill) => {
  isEdit.value = true
  Object.assign(currentBill, bill)
}

// 保存账单
const saveBill = () => {
  billFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await billService.updateBill(currentBill.id, currentBill)
          ElMessage.success('账单更新成功')
        } else {
          // 设置当前用户ID
          currentBill.userId = currentUser.value.id
          await billService.createBill(currentBill)
          ElMessage.success('账单创建成功')
        }
        resetForm()
        loadBills()
      } catch (error) {
        ElMessage.error('保存账单失败: ' + error.message)
      }
    } else {
      return false
    }
  })
}

// 删除账单
const deleteBill = (id) => {
  ElMessageBox.confirm('确认删除该账单吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await billService.deleteBill(id)
      ElMessage.success('账单删除成功')
      loadBills()
    } catch (error) {
      ElMessage.error('删除账单失败: ' + error.message)
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 重置表单
const resetForm = () => {
  billFormRef.value.resetFields()
  currentBill.id = null
  currentBill.userId = currentUser.value ? currentUser.value.id : null
  currentBill.date = new Date().toISOString().slice(0, 10)
  currentBill.accountTypeId = null
  currentBill.amount = null
  currentBill.ledgerId = null
  currentBill.remark = ''
  isEdit.value = false
}

// 分页相关方法
const handleSizeChange = (val) => {
  pageSize.value = val
  loadBills()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadBills()
}

// 组件挂载时加载数据
onMounted(() => {
  // 获取当前用户
  currentUser.value = authService.getCurrentUser()
  
  if (currentUser.value && currentUser.value.id) {
    // 初始化表单中的用户ID
    currentBill.userId = currentUser.value.id
    // 加载数据
    loadBills()
    loadAccountTypes()
    loadLedgers()
  } else {
    ElMessage.error('未登录，请先登录')
    // 可以考虑跳转到登录页面
  }
})
</script>

<style scoped>
.bill-list {
  padding: 20px;
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
</style>