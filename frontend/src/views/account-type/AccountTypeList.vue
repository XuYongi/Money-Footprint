<template>
  <div class="account-type-list">
    <el-card class="account-type-card">
      <template #header>
        <div class="card-header">
          <span>账目类型</span>
          <el-button type="primary" @click="handleAddAccountType">新增类型</el-button>
        </div>
      </template>
      
      <el-table :data="accountTypes" style="width: 100%" v-loading="loading" row-key="id">
        <el-table-column prop="name" label="类型名称" />
        <el-table-column prop="categoryName" label="收支类型" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.category === 1 ? 'success' : 'danger'">
              {{ scope.row.categoryName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="levelName" label="级别" width="80" />
        <el-table-column prop="parentName" label="父级类型" width="150" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEditAccountType(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDeleteAccountType(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 账目类型表单对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="currentAccountType" label-width="80px">
        <el-form-item label="类型名称" prop="name" :rules="[{ required: true, message: '请输入类型名称' }]">
          <el-input v-model="currentAccountType.name" placeholder="请输入类型名称" />
        </el-form-item>
        <el-form-item label="收支类型" prop="category" :rules="[{ required: true, message: '请选择收支类型' }]">
          <el-radio-group v-model="currentAccountType.category">
            <el-radio :label="0">支出</el-radio>
            <el-radio :label="1">收入</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="级别" prop="level" :rules="[{ required: true, message: '请选择级别' }]">
          <el-radio-group v-model="currentAccountType.level">
            <el-radio :label="1">一级分类</el-radio>
            <el-radio :label="2">二级分类</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="父级类型" v-if="currentAccountType.level === 2">
          <el-select v-model="currentAccountType.parentId" placeholder="请选择父级类型" clearable>
            <el-option
              v-for="item in parentAccountTypes"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveAccountType">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import accountTypeService from '@/services/accountTypeService'

// 数据列表
const accountTypes = ref([])
const parentAccountTypes = ref([])

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)

// 当前编辑的账目类型
const currentAccountType = reactive({
  id: null,
  name: '',
  category: 0,
  level: 1,
  parentId: null,
  categoryName: '',
  levelName: ''
})

// 计算属性
const categoryMap = {
  0: { name: '支出', type: 'danger' },
  1: { name: '收入', type: 'success' }
}

const levelMap = {
  1: '一级分类',
  2: '二级分类'
}

// 方法
const handleAddAccountType = () => {
  dialogTitle.value = '新增类型'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEditAccountType = (accountType) => {
  dialogTitle.value = '编辑类型'
  isEdit.value = true
  // 处理数据映射
  const editData = { ...accountType }
  editData.categoryName = categoryMap[editData.category]?.name || ''
  editData.levelName = levelMap[editData.level] || ''
  Object.assign(currentAccountType, editData)
  dialogVisible.value = true
}

const handleDeleteAccountType = (accountType) => {
  ElMessageBox.confirm(
    `确定要删除账目类型"${accountType.name}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    deleteAccountType(accountType.id)
  }).catch(() => {
    // 取消删除
  })
}

const saveAccountType = () => {
  // 处理保存前的数据
  currentAccountType.categoryName = categoryMap[currentAccountType.category]?.name || ''
  currentAccountType.levelName = levelMap[currentAccountType.level] || ''
  
  if (isEdit.value) {
    updateAccountType()
  } else {
    createAccountType()
  }
}

// 创建账目类型
const createAccountType = async () => {
  try {
    loading.value = true
    // 假设用户ID为1，实际项目中应该从登录信息中获取
    const accountTypeData = {
      ...currentAccountType,
      userId: 1
    }
    const response = await accountTypeService.createAccountType(accountTypeData)
    accountTypes.value.push({
      ...response.data,
      categoryName: categoryMap[response.data.category]?.name || '',
      levelName: levelMap[response.data.level] || ''
    })
    ElMessage.success('新增成功')
    dialogVisible.value = false
  } catch (error) {
    console.error('创建账目类型失败:', error)
    ElMessage.error('新增失败: ' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

// 更新账目类型
const updateAccountType = async () => {
  try {
    loading.value = true
    const accountTypeData = {
      ...currentAccountType,
      userId: 1
    }
    const response = await accountTypeService.updateAccountType(accountTypeData)
    const index = accountTypes.value.findIndex(item => item.id === currentAccountType.id)
    if (index > -1) {
      accountTypes.value[index] = {
        ...response.data,
        categoryName: categoryMap[response.data.category]?.name || '',
        levelName: levelMap[response.data.level] || ''
      }
      ElMessage.success('更新成功')
      dialogVisible.value = false
    }
  } catch (error) {
    console.error('更新账目类型失败:', error)
    ElMessage.error('更新失败: ' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

// 删除账目类型
const deleteAccountType = async (id) => {
  try {
    loading.value = true
    await accountTypeService.deleteAccountType(id)
    const index = accountTypes.value.findIndex(item => item.id === id)
    if (index > -1) {
      accountTypes.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  } catch (error) {
    console.error('删除账目类型失败:', error)
    ElMessage.error('删除失败: ' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  Object.assign(currentAccountType, {
    id: null,
    name: '',
    category: 0,
    level: 1,
    parentId: null,
    categoryName: '',
    levelName: ''
  })
}

// 加载账目类型数据
const loadAccountTypes = async () => {
  try {
    loading.value = true
    // 假设用户ID为1，实际项目中应该从登录信息中获取
    const response = await accountTypeService.getAccountTypesByUserId(1)
    accountTypes.value = response.data.map(item => ({
      ...item,
      categoryName: categoryMap[item.category]?.name || '',
      levelName: levelMap[item.level] || ''
    }))
    
    // 筛选出一级分类作为父级选项
    parentAccountTypes.value = accountTypes.value.filter(item => item.level === 1)
  } catch (error) {
    console.error('加载账目类型失败:', error)
    ElMessage.error('数据加载失败: ' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadAccountTypes()
})
</script>

<style scoped>
.account-type-card {
  min-height: calc(100vh - 140px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>