# Money-Footprint（账迹）

<p align="center">
  <img width="180" src="https://raw.githubusercontent.com/fenixsoft/awesome-fenix/master/.vuepress/public/images/logo-color.png" alt="logo">
</p>

<p align="center">
  <a href="https://travis-ci.com/XuYongi/Money-Footprint" target="_blank">
    <img src="https://travis-ci.com/XuYongi/Money-Footprint.svg?branch=master" alt="Travis-CI">
  </a>
  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
    <img src="https://raw.githubusercontent.com/fenixsoft/awesome-fenix/master/.vuepress/public/images/License-Apache.svg" alt="License">
  </a>
  <a href="https://creativecommons.org/licenses/by/4.0/" target="_blank">
    <img src="https://raw.githubusercontent.com/fenixsoft/awesome-fenix/master/.vuepress/public/images/DocLicense-CC-red.svg" alt="Document License">
  </a>
</p>

Money-Footprint（账迹）是一个基于Spring Boot和Vue 3的个人账单管理应用，帮助用户记录和分析日常收支情况。

## 功能特性

- **用户管理**：用户注册、登录和认证
- **账单管理**：记录、查看、编辑和删除账单
- **账目类型**：自定义收入和支出类型
- **账本管理**：创建和管理多个账本
- **账本共享**：与他人共享账本，共同记账
- **数据统计**：可视化展示收支情况和趋势分析
- **响应式设计**：支持PC和移动端访问

## 技术架构

### 后端技术栈

- **Spring Boot 2.x**：Java Web框架
- **Spring Data JPA**：数据持久化
- **Spring Security**：安全认证和授权
- **PostgreSQL**：关系型数据库
- **JWT**：JSON Web Token认证
- **Maven**：项目构建工具

### 前端技术栈

- **Vue 3**：渐进式JavaScript框架
- **Element Plus**：Vue 3组件库
- **Vite**：前端构建工具
- **Axios**：HTTP客户端
- **Pinia**：状态管理

## 运行程序

### 通过Docker容器方式运行

```bash
$ docker run -d -p 8080:8080 --name money-footprint icyfenix/bookstore:monolithic 
```

然后在浏览器访问：[http://localhost:8080](http://localhost:8080)，系统预置了一个用户（user:icyfenix，pw:123456），也可以注册新用户来测试。

默认会使用HSQLDB的内存模式作为数据库，并在系统启动时自动初始化好了Schema，完全开箱即用。但这同时也意味着当程序运行结束时，所有的数据都将不会被保留。

如果希望使用PostgreSQL数据库，可以使用环境变量"PROFILES"来激活SpringBoot中针对PostgreSQL所提供的配置，命令如下所示：

```bash
$ docker run -d -p 8080:8080 --name money-footprint icyfenix/bookstore:monolithic -e PROFILES=pgsql
```

此时你需要通过Docker link、Docker Compose或者直接在主机的Host文件中提供一个名为"pgsql_lan"的DNS映射，使程序能顺利链接到数据库。

### 通过Git上的源码，以Maven运行

```bash
# 克隆获取源码
$ git clone https://github.com/XuYongi/Money-Footprint.git

# 进入工程根目录
$ cd Money-Footprint

# 编译打包
# 采用Maven Wrapper，此方式只需要机器安装有JDK 8或以上版本即可，无需包括Maven在内的其他任何依赖
# 如在Windows下应使用mvnw.cmd package代替以下命令
$ ./mvnw package

# 运行程序，地址为localhost:8080
$ java -jar target/bookstore-1.0.0-Monolithic-SNAPSHOT.jar
```

然后在浏览器访问：[http://localhost:8080](http://localhost:8080)，系统预置了一个用户（user:icyfenix，pw:123456），也可以注册新用户来测试。

### 通过Git上的源码，在IDE环境中运行

1. 以IntelliJ IDEA为例，Git克隆本项目后，在File -> Open菜单选择本项目所在的目录，或者pom.xml文件，以Maven方式导入工程。

2. IDEA将自动识别出这是一个SpringBoot工程，并定位启动入口为BillApplication，待IDEA内置的Maven自动下载完所有的依赖包后，运行该类即可启动。

3. 如你使用其他的IDE，没有对SpringBoot的直接支持，亦可自行定位到BillApplication，这是一个带有main()方法的Java类，运行即可。

4. 可通过IDEA的Maven面板中Lifecycle里面的package来对项目进行打包、发布。

## 开发环境配置

有关详细的开发环境配置，请参考 [DEVELOPMENT_ENVIRONMENT.md](DEVELOPMENT_ENVIRONMENT.md) 文件。

您也可以使用项目提供的脚本来快速启动项目：

### Windows系统
```cmd
# 启动后端服务
run_backend.bat

# 启动前端开发服务器
run_frontend.bat
```

### Mac/Linux系统
```bash
# 启动后端服务
./run_backend.sh

# 启动前端开发服务器
./run_frontend.sh
```

## 前端开发环境配置

本项目现在包含了基于Vue 3 + Element Plus的前端项目，位于`frontend`目录中。

### 环境准备

1. 安装Node.js (版本16+)
   - 访问 https://nodejs.org/ 下载并安装Node.js
   - 验证安装：在终端中运行 `node --version` 和 `npm --version`

2. 安装项目依赖
   ```bash
   # 进入前端目录
   cd frontend
   
   # 安装依赖
   npm install
   ```

3. 运行开发服务器
   ```bash
   # 启动开发服务器
   npm run dev
   ```
   
   启动后访问 http://localhost:3000 查看前端页面

### IntelliJ IDEA配置

为了在IntelliJ IDEA中获得最佳的Vue 3开发体验，请安装以下插件：

1. **Vue.js** - 官方Vue.js插件，提供语法高亮和智能提示
2. **Element Plus** - Element Plus组件库支持
3. **JavaScript and TypeScript** - JavaScript和TypeScript语言支持
4. **CSS** - CSS语言支持

配置步骤：

1. 打开IDEA，进入 `File` -> `Settings` -> `Plugins`
2. 搜索并安装上述插件
3. 重启IDEA
4. 打开项目中的`frontend`目录
5. IDEA会自动识别Vue项目并应用相应配置

### 项目结构

```
frontend/
├── public/              # 静态资源
├── src/                 # 源代码
│   ├── assets/          # 静态资源
│   ├── components/      # 公共组件
│   ├── views/           # 页面视图
│   ├── router/          # 路由配置
│   ├── store/           # 状态管理
│   ├── services/        # API服务封装
│   ├── utils/           # 工具函数
│   ├── App.vue          # 根组件
│   └── main.js          # 入口文件
├── index.html           # HTML模板
├── package.json         # 项目配置
├── vite.config.js       # 构建配置
├── .eslintrc.cjs        # ESLint配置
└── .prettierrc.json     # Prettier配置
```

### 开发指南

1. **组件开发**
   - 使用单文件组件（.vue文件）
   - 遵循Vue 3 Composition API
   - 使用Element Plus组件库

2. **路由管理**
   - 路由配置位于`src/router/index.js`
   - 添加新页面时需要在路由中注册

3. **状态管理**
   - 使用Pinia进行状态管理
   - 状态管理文件位于`src/store/`

4. **API调用**
   - 使用axios进行HTTP请求
   - API服务封装位于`src/services/`

5. **代码规范**
   - 使用ESLint和Prettier保证代码风格一致
   - 提交代码前运行`npm run lint`检查代码规范

### 构建和部署

```bash
# 构建生产版本
npm run build

# 预览生产构建
npm run preview
```

## 数据库设计

项目使用PostgreSQL数据库，主要包含以下表结构：

- `user_t`：用户表
- `account_type_t`：账目类型表
- `ledger_t`：账本表
- `ledger_member_t`：账本成员表
- `bill_t`：账单表

所有表都包含审计字段（创建时间、创建人、修改时间、修改人）以追踪数据变更历史。

## API文档

项目使用Postman集合进行API测试，相关文件位于`src/main/resources/ledger_postman_collection.json`。

## 部署

项目支持Docker容器化部署，通过Travis CI进行持续集成和持续部署。

## 协议

- 本文档代码部分采用[Apache 2.0协议](https://www.apache.org/licenses/LICENSE-2.0)进行许可。遵循许可的前提下，你可以自由地对代码进行修改，再发布，可以将代码用作商业用途。但要求你：
  - **署名**：在原有代码和衍生代码中，保留原作者署名及代码来源信息。
  - **保留许可证**：在原有代码和衍生代码中，保留Apache 2.0协议文件。
  
- 本作品文档部分采用[知识共享署名 4.0 国际许可协议](http://creativecommons.org/licenses/by/4.0/)进行许可。 遵循许可的前提下，你可以自由地共享，包括在任何媒介上以任何形式复制、发行本作品，亦可以自由地演绎、修改、转换或以本作品为基础进行二次创作。但要求你：
  - **署名**：应在使用本文档的全部或部分内容时候，注明原作者及来源信息。
  - **非商业性使用**：不得用于商业出版或其他任何带有商业性质的行为。如需商业使用，请联系作者。
  - **相同方式共享的条件**：在本文档基础上演绎、修改的作品，应当继续以知识共享署名 4.0国际许可协议进行许可。
