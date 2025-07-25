<template>
  <div class="home">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>欢迎使用账簿管理系统</span>
        </div>
      </template>
      
      <div class="welcome-content">
        <h2>功能介绍</h2>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="feature-card">
              <div class="feature-content">
                <h3><i class="el-icon-document"></i> 账单管理</h3>
                <p>记录您的每一笔收入和支出，方便随时查看和管理个人财务状况。</p>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="12">
            <el-card class="feature-card">
              <div class="feature-content">
                <h3><i class="el-icon-notebook-2"></i> 账本管理</h3>
                <p>创建不同的账本分类管理您的财务，如家庭账本、旅行账本等。</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="12">
            <el-card class="feature-card">
              <div class="feature-content">
                <h3><i class="el-icon-collection"></i> 账目类型</h3>
                <p>自定义收入和支出类型，更好地分类和统计您的财务数据。</p>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="12">
            <el-card class="feature-card">
              <div class="feature-content">
                <h3><i class="el-icon-data-analysis"></i> 统计分析</h3>
                <p>通过图表直观展示您的财务状况，帮助您更好地规划和管理资金。</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <div class="quick-links">
          <h3>快速开始</h3>
          <el-button type="primary" @click="$router.push('/bills')">开始记账</el-button>
          <el-button type="success" @click="$router.push('/statistics')">查看统计</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
// 首页组件不需要额外的逻辑
</script>

<style scoped>
.home {
  padding: 20px;
}

.card-header {
  text-align: center;
}

.welcome-content h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #409EFF;
}

.feature-card {
  height: 150px;
}

.feature-content {
  text-align: center;
}

.feature-content h3 {
  margin-bottom: 15px;
  color: #606266;
}

.feature-content i {
  margin-right: 10px;
  font-size: 20px;
  color: #409EFF;
}

.feature-content p {
  color: #909399;
  line-height: 1.6;
}

.quick-links {
  text-align: center;
  margin-top: 30px;
}

.quick-links h3 {
  margin-bottom: 20px;
}
</style>