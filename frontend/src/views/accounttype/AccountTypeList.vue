<template>
  <div class="account-type-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>账目类型列表</span>
          <el-button type="primary" @click="showAddForm">新增类型</el-button>
        </div>
      </template>
      
      <el-table :data="accountTypes" style="width: 100%" row-key="id" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" v-loading="loading">
        <el-table-column prop="name" label="类型名称" />
        <el-table-column prop="categoryName" label="收支类型" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="mini" @click="showAddChildForm(scope.row)">添加子类</el-button>
            <el-button size="mini" @click="showEditForm(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="deleteAccountType(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 账目类型表单对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="currentAccountType" :rules="rules" ref="accountTypeForm" label-width="100px">
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="currentAccountType.name" placeholder="请输入类型名称" />
        </el-form-item>
        
        <el-form-item label="收支类型" prop="category">
          <el-select v-model="currentAccountType.category" placeholder="请选择收支类型" style="width: 100%">
            <el-option label="支出" :value="0" />
            <el-option label="收入" :value="1" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="父级类型" prop="parentId">
          <el-tree-select
            v-model="currentAccountType.parentId"
            :data="accountTypeTree"
            node-key="id"
            :props="{ label: 'name', children: 'children' }"
            placeholder="请选择父级类型"
            style="width: 100%"
            clearable
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveAccountType">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import accountTypeService from '@/services/accountTypeService'
import authService from '@/services/authService'

// 数据
const accountTypes = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const isAddChild = ref(false)
const currentParentId = ref(null)
const currentUser = ref(null)

// 当前账目类型
const currentAccountType = reactive({
  id: null,
  name: '',
  userId: null,
  level: 1,
  parentId: null,
  category: 0
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入类型名称', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择收支类型', trigger: 'change' }
  ]
}

// 表单引用
const accountTypeFormRef = ref(null)

// 对话框标题
const dialogTitle = computed(() => {
  if (isAddChild.value) return '新增子类型'
  return isEdit.value ? '编辑类型' : '新增类型'
})

// 账目类型树形数据
const accountTypeTree = computed(() => {
  return accountTypes.value.map(type => {
    return {
      id: type.id,
      name: type.name,
      children: type.children
    }
  })
})

// 显示新增表单
const showAddForm = () => {
  isEdit.value = false
  isAddChild.value = false
  resetForm()
  dialogVisible.value = true
}

// 显示添加子类表单
const showAddChildForm = (parent) => {
  isEdit.value = false
  isAddChild.value = true
  resetForm()
  currentAccountType.parentId = parent.id
  currentAccountType.level = parent.level + 1
  dialogVisible.value = true
}

// 显示编辑表单
const showEditForm = (accountType) => {
  isEdit.value = true
  isAddChild.value = false
  Object.assign(currentAccountType, accountType)
  dialogVisible.value = true
}

// 加载账目类型列表
const loadAccountTypes = async () => {
  try {
    loading.value = true
    const response = await accountTypeService.getAccountTypesByUserId(currentUser.value.id)
    accountTypes.value = enhanceAccountTypesWithCategory(response.data)
  } catch (error) {
    ElMessage.error('加载账目类型失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 为账目类型添加分类名称
const enhanceAccountTypesWithCategory = (types) => {
  return types.map(type => {
    return {
      ...type,
      categoryName: type.category === 1 ? '收入' : '支出',
      children: type.children ? enhanceAccountTypesWithCategory(type.children) : []
    }
  })
}

// 保存账目类型
const saveAccountType = () => {
  accountTypeFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 设置用户ID
        currentAccountType.userId = currentUser.value.id
        
        if (isEdit.value) {
          await accountTypeService.updateAccountType(currentAccountType.id, currentAccountType)
          ElMessage.success('账目类型更新成功')
        } else {
          await accountTypeService.createAccountType(currentAccountType)
          ElMessage.success('账目类型创建成功')
        }
        dialogVisible.value = false
        loadAccountTypes()
      } catch (error) {
        ElMessage.error('保存账目类型失败: ' + error.message)
      }
    } else {
      return false
    }
  })
}

// 删除账目类型
const deleteAccountType = (id) => {
  ElMessageBox.confirm('确认删除该账目类型吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await accountTypeService.deleteAccountType(id)
      ElMessage.success('账目类型删除成功')
      loadAccountTypes()
    } catch (error) {
      ElMessage.error('删除账目类型失败: ' + error.message)
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 重置表单
const resetForm = () => {
  accountTypeFormRef.value.resetFields()
  currentAccountType.id = null
  currentAccountType.name = ''
  currentAccountType.userId = currentUser.value ? currentUser.value.id : null
  currentAccountType.level = 1
  currentAccountType.parentId = null
  currentAccountType.category = 0
  isEdit.value = false
  isAddChild.value = false
}

// 组件挂载时加载数据
onMounted(() => {
  // 获取当前用户
  currentUser.value = authService.getCurrentUser()
  
  if (currentUser.value) {
    // 初始化表单中的用户ID
    currentAccountType.userId = currentUser.value.id
    // 加载数据
    loadAccountTypes()
  } else {
    ElMessage.error('未登录，请先登录')
    // 可以考虑跳转到登录页面
  }
})
</script>

<style scoped>
.account-type-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>