<template>
  <div class="ledger-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>账本列表</span>
          <el-button type="primary" @click="showAddForm">新增账本</el-button>
        </div>
      </template>
      
      <el-table :data="ledgers" style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="账本名称" />
        <el-table-column prop="creatorName" label="创建人" />
        <el-table-column label="操作" width="250">
          <template #default="scope">
            <el-button size="mini" @click="showEditForm(scope.row)">编辑</el-button>
            <el-button size="mini" type="primary" @click="showMembers(scope.row)">成员管理</el-button>
            <el-button size="mini" type="danger" @click="deleteLedger(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 账本表单对话框 -->
    <el-dialog :title="isEdit ? '编辑账本' : '新增账本'" v-model="dialogVisible" width="500px">
      <el-form :model="currentLedger" :rules="rules" ref="ledgerForm" label-width="100px">
        <el-form-item label="账本名称" prop="name">
          <el-input v-model="currentLedger.name" placeholder="请输入账本名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveLedger">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 成员管理对话框 -->
    <el-dialog title="成员管理" v-model="memberDialogVisible" width="600px">
      <el-table :data="ledgerMembers" style="width: 100%">
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button 
              v-if="scope.row.id !== currentUser.id" 
              size="mini" 
              type="danger" 
              @click="removeMember(scope.row.id)"
            >
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div style="margin-top: 20px;">
        <el-input 
          v-model="newMemberUsername" 
          placeholder="请输入要添加的用户名" 
          style="width: 200px; margin-right: 10px;"
        />
        <el-button type="primary" @click="addMember">添加成员</el-button>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="memberDialogVisible = false">关 闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import ledgerService from '@/services/ledgerService'
import ledgerMemberService from '@/services/ledgerMemberService'
import userService from '@/services/userService'
import authService from '@/services/authService'

// 数据
const ledgers = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const memberDialogVisible = ref(false)
const isEdit = ref(false)
const currentLedgerId = ref(null)
const ledgerMembers = ref([])
const newMemberUsername = ref('')
const currentUser = ref(null)

// 当前账本
const currentLedger = reactive({
  id: null,
  name: '',
  creatorId: null
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入账本名称', trigger: 'blur' }
  ]
}

// 表单引用
const ledgerFormRef = ref(null)

// 显示新增表单
const showAddForm = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 显示编辑表单
const showEditForm = (ledger) => {
  isEdit.value = true
  Object.assign(currentLedger, ledger)
  dialogVisible.value = true
}

// 显示成员管理
const showMembers = async (ledger) => {
  currentLedgerId.value = ledger.id
  memberDialogVisible.value = true
  await loadLedgerMembers(ledger.id)
}

// 加载账本列表
const loadLedgers = async () => {
  try {
    loading.value = true
    const response = await ledgerService.getLedgersByUserId(currentUser.value.id)
    ledgers.value = await enhanceLedgersWithCreator(response.data)
  } catch (error) {
    ElMessage.error('加载账本列表失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 为账本添加创建人信息
const enhanceLedgersWithCreator = async (ledgers) => {
  // 获取所有用户信息
  const userResponse = await userService.getUsers()
  const users = userResponse.data
  
  return ledgers.map(ledger => {
    const creator = users.find(user => user.id === ledger.creatorId)
    return {
      ...ledger,
      creatorName: creator ? creator.username : '未知用户'
    }
  })
}

// 加载账本成员
const loadLedgerMembers = async (ledgerId) => {
  try {
    const response = await ledgerMemberService.getMembersByLedgerId(ledgerId)
    // 获取成员详细信息
    const userResponse = await userService.getUsers()
    const users = userResponse.data
    
    ledgerMembers.value = response.data.map(member => {
      const user = users.find(u => u.id === member.userId)
      return user ? { ...user, ledgerMemberId: member.id } : null
    }).filter(member => member !== null)
  } catch (error) {
    ElMessage.error('加载账本成员失败: ' + error.message)
  }
}

// 保存账本
const saveLedger = () => {
  ledgerFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await ledgerService.updateLedger(currentLedger.id, currentLedger)
          ElMessage.success('账本更新成功')
        } else {
          // 设置创建人ID为当前用户ID
          currentLedger.creatorId = currentUser.value.id
          await ledgerService.createLedger(currentLedger)
          ElMessage.success('账本创建成功')
        }
        dialogVisible.value = false
        loadLedgers()
      } catch (error) {
        ElMessage.error('保存账本失败: ' + error.message)
      }
    } else {
      return false
    }
  })
}

// 删除账本
const deleteLedger = (id) => {
  ElMessageBox.confirm('确认删除该账本吗？删除账本将同时删除该账本下的所有账单。', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await ledgerService.deleteLedger(id)
      ElMessage.success('账本删除成功')
      loadLedgers()
    } catch (error) {
      ElMessage.error('删除账本失败: ' + error.message)
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 添加成员
const addMember = async () => {
  if (!newMemberUsername.value) {
    ElMessage.warning('请输入用户名')
    return
  }
  
  try {
    // 查找用户
    const userResponse = await userService.getUsers()
    const user = userResponse.data.find(u => u.username === newMemberUsername.value)
    
    if (!user) {
      ElMessage.error('用户不存在')
      return
    }
    
    // 添加成员
    await ledgerMemberService.addMember(currentLedgerId.value, user.id)
    ElMessage.success('成员添加成功')
    newMemberUsername.value = ''
    loadLedgerMembers(currentLedgerId.value)
  } catch (error) {
    ElMessage.error('添加成员失败: ' + error.message)
  }
}

// 移除成员
const removeMember = async (userId) => {
  try {
    const member = ledgerMembers.value.find(m => m.id === userId)
    if (member) {
      await ledgerMemberService.removeMember(member.ledgerMemberId)
      ElMessage.success('成员移除成功')
      loadLedgerMembers(currentLedgerId.value)
    }
  } catch (error) {
    ElMessage.error('移除成员失败: ' + error.message)
  }
}

// 重置表单
const resetForm = () => {
  ledgerFormRef.value.resetFields()
  currentLedger.id = null
  currentLedger.name = ''
  currentLedger.creatorId = currentUser.value ? currentUser.value.id : null
  isEdit.value = false
}

// 组件挂载时加载数据
onMounted(() => {
  // 获取当前用户
  currentUser.value = authService.getCurrentUser()
  
  if (currentUser.value) {
    // 初始化表单中的创建人ID
    currentLedger.creatorId = currentUser.value.id
    // 加载数据
    loadLedgers()
  } else {
    ElMessage.error('未登录，请先登录')
    // 可以考虑跳转到登录页面
  }
})
</script>

<style scoped>
.ledger-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>