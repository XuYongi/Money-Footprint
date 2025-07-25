# 开发环境配置指南

本文档将指导您如何配置本项目的开发环境，包括后端(Spring Boot)和前端(Vue 3)。

## 1. 环境要求

### 1.1 后端环境要求
- **JDK 8 或以上版本**
- **Maven 3.6+** (项目包含Maven Wrapper，可直接使用)
- **IDE**: IntelliJ IDEA Ultimate (推荐) 或其他支持Spring Boot的IDE
- **数据库**: HSQLDB(默认)、MySQL 或 PostgreSQL

### 1.2 前端环境要求
- **Node.js 16+**
- **npm 8+** 或 **yarn**
- **IDE**: IntelliJ IDEA Ultimate/WebStorm 或 VS Code
- **浏览器**: Chrome/Firefox/Safari 等现代浏览器

## 2. 后端环境配置

### 2.1 安装JDK
1. 访问 [Oracle JDK下载页面](https://www.oracle.com/java/technologies/downloads/) 或 [OpenJDK](https://openjdk.org/)
2. 下载并安装JDK 8或更高版本
3. 配置环境变量:
   - `JAVA_HOME`: JDK安装目录
   - `PATH`: 添加 `%JAVA_HOME%\bin`

### 2.2 安装Maven (可选)
项目提供了Maven Wrapper，可以直接使用：
- Windows: `mvnw.cmd`
- Linux/Mac: `./mvnw`

如需独立安装Maven:
1. 访问 [Maven官网](https://maven.apache.org/download.cgi)
2. 下载并解压Maven
3. 配置环境变量:
   - `MAVEN_HOME`: Maven安装目录
   - `PATH`: 添加 `%MAVEN_HOME%\bin`

### 2.3 配置IntelliJ IDEA后端开发环境
1. 打开IntelliJ IDEA
2. 选择 `File` -> `Open`，选择项目根目录或`pom.xml`文件
3. IDEA会自动识别为Maven项目并导入依赖
4. 安装推荐插件:
   - **Spring Boot Plugin**: Spring Boot开发支持
   - **Lombok Plugin**: Lombok注解支持
   - **JPA Plugin**: JPA实体支持
   - **通义灵码**: AI辅助编程插件(可选)

5. 配置项目SDK:
   - 打开 `File` -> `Project Structure`
   - 在 `Project` 选项卡中设置 `Project SDK` 为已安装的JDK
   - 设置 `Project language level` 为 8 或更高

6. 配置运行环境:
   - 找到 `com.fx.billsoft.BillApplication` 类
   - 右键点击 `Run BillApplication` 或点击类中的main方法旁的绿色运行按钮

## 3. 前端环境配置

### 3.1 安装Node.js
1. 访问 [Node.js官网](https://nodejs.org/)
2. 下载并安装LTS版本(推荐16+版本)
3. 验证安装:
   ```bash
   node --version
   npm --version
   ```

### 3.2 安装前端依赖
1. 进入前端目录:
   ```bash
   cd frontend
   ```
2. 安装项目依赖:
   ```bash
   # 使用npm
   npm install
   
   # 或使用yarn
   yarn install
   ```

### 3.3 配置IntelliJ IDEA前端开发环境
1. 打开IntelliJ IDEA
2. 选择 `File` -> `Open`，选择`frontend`目录
3. 安装推荐插件:
   - **Vue.js**: Vue.js开发支持
   - **Element Plus**: Element UI组件库支持
   - **JavaScript and TypeScript**: JavaScript/TypeScript语言支持
   - **CSS**: CSS语言支持
   - **通义灵码**: AI辅助编程插件(可选)

4. 配置Node.js环境:
   - 打开 `File` -> `Settings` -> `Languages & Frameworks` -> `Node.js and NPM`
   - 设置 `Node interpreter` 为已安装的Node.js路径
   - 点击 `Enable` 启用Node.js编码协助

5. 配置代码格式化:
   - ESLint会自动启用
   - Prettier需要手动配置:
     - 打开 `File` -> `Settings` -> `Languages & Frameworks` -> `JavaScript` -> `Prettier`
     - 设置 `Prettier package` 为项目中的 `node_modules/prettier`

## 4. 运行项目

### 4.1 运行后端服务
使用Maven Wrapper运行:
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

或在IntelliJ IDEA中直接运行`BillApplication`类。

后端服务将在 `http://localhost:8080` 启动。

### 4.2 运行前端开发服务器
```bash
cd frontend
# 使用npm
npm run dev

# 或使用yarn
yarn dev
```

前端开发服务器将在 `http://localhost:3000` 启动。

## 5. 开发工具推荐

### 5.1 IDE推荐
- **IntelliJ IDEA Ultimate**: 同时支持Java后端和Vue前端开发
- **VS Code**: 轻量级前端开发工具，配合通义灵码使用效果更佳

### 5.2 浏览器开发工具
- **Chrome DevTools**: 调试前端应用
- **Postman**: 测试后端API接口

### 5.3 版本控制
- **Git**: 项目已配置.gitignore文件

## 6. 常见问题解决

### 6.1 端口占用问题
如果默认端口被占用，可以通过以下方式更改:
- 后端: 在 `application.yml` 中修改 `server.port`
- 前端: 在 `vite.config.js` 中修改 `server.port`

### 6.2 数据库配置
项目默认使用HSQLDB内存数据库，如需切换到MySQL或PostgreSQL:
1. 修改 `application.yml` 中的 `spring.profiles.active`
2. 在对应的配置文件中修改数据库连接信息

### 6.3 依赖下载问题
如果Maven依赖下载失败:
1. 检查网络连接
2. 配置国内镜像源(如阿里云)
3. 清理Maven缓存后重新下载

## 7. 项目结构说明

### 7.1 后端项目结构
```
src/
├── main/
│   ├── java/
│   │   └── com/fx/billsoft/
│   │       ├── application/     # 应用服务层
│   │       ├── domain/          # 领域模型层
│   │       ├── infrastructure/  # 基础设施层
│   │       ├── interfaces/      # 接口层
│   │       └── BillApplication.java # 启动类
│   └── resources/
│       ├── db/                  # 数据库脚本
│       └── application.yml      # 配置文件
└── test/                        # 测试代码
```

### 7.2 前端项目结构
```
frontend/
├── public/                      # 静态资源
├── src/
│   ├── assets/                  # 静态资源
│   ├── components/              # 公共组件
│   ├── views/                   # 页面视图
│   ├── router/                  # 路由配置
│   ├── store/                   # 状态管理
│   ├── services/                # API服务封装
│   ├── utils/                   # 工具函数
│   ├── App.vue                  # 根组件
│   └── main.js                  # 入口文件
├── index.html                   # HTML模板
├── package.json                 # 项目配置
└── vite.config.js               # 构建配置
```

## 8. 开发流程

### 8.1 后端开发流程
1. 根据需求创建实体类(在`domain/entity/`目录下)
2. 创建Repository接口(在`infrastructure/repository/`目录下)
3. 创建Service类(在`domain/service/`目录下)
4. 创建AppService类(在`application/`目录下)
5. 创建Controller类(在`interfaces/`目录下)
6. 编写单元测试

### 8.2 前端开发流程
1. 根据需求创建页面组件(在`src/views/`目录下)
2. 创建可复用组件(在`src/components/`目录下)
3. 添加路由配置(在`src/router/`目录下)
4. 创建API服务(在`src/services/`目录下)
5. 配置状态管理(在`src/store/`目录下)

## 9. 代码规范

### 9.1 后端代码规范
- 遵循DDD分层架构
- 使用Lombok简化代码
- 使用统一的异常处理机制
- 编写单元测试

### 9.2 前端代码规范
- 使用Vue 3 Composition API
- 遵循ESLint和Prettier规范
- 使用Element Plus组件库
- 组件命名遵循PascalCase

通过以上配置，您就可以开始愉快地开发了！