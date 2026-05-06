# 电商平台管理系统

一个基于 Spring Boot + Vue 3 + Uni-app 的全栈电商平台，包含管理后台（Web）、用户端（Web）和微信小程序。

## 技术栈

| 模块 | 技术 |
|------|------|
| 后端 | Spring Boot 3.2、Java 21、MyBatis-Plus、Spring Security、JWT、Redis、H2/MySQL、Druid |
| 管理后台 | Vue 3、Vite、Element Plus、Axios、Vue Router、Pinia |
| 用户端 | Vue 3、Vite（同上技术栈） |
| 小程序 | Uni-app（Vue 3）、支持 H5 和微信小程序 |

## 项目结构

```
├── backend/            # 后端 Spring Boot 项目
│   └── src/main/java/com/example/ecommerce/
│       ├── config/          # Spring Security、MyBatis-Plus、Redis 等配置
│       ├── controller/      # 接口控制器
│       ├── dto/             # 数据传输对象
│       ├── entity/          # 数据库实体
│       ├── mapper/          # MyBatis-Plus 数据访问层
│       ├── service/         # 业务逻辑层
│       └── utils/           # JWT 等工具类
├── frontend/           # Web 前端（管理后台 + 用户端）
│   └── src/
│       ├── views/           # 管理后台页面（登录、仪表盘、商品/分类/订单/用户管理）
│       └── client/          # 用户端页面（首页、商品详情、购物车、结算、订单）
├── uniapp/             # Uni-app 小程序 / H5
│   └── src/pages/           # 首页、详情、购物车、订单、用户、结算
├── start.bat           # Windows 一键启动脚本
├── start.sh            # Linux / macOS 一键启动脚本
└── stop.sh             # 停止服务脚本
```

## 功能模块

### 管理后台
- **仪表盘** — 数据概览
- **商品管理** — 商品增删改查
- **分类管理** — 商品分类维护
- **订单管理** — 订单查看与处理
- **用户管理** — 用户信息管理
- **登录认证** — JWT + Spring Security 权限控制

### 用户端（Web / 小程序）
- 商品浏览（首页、分类）
- 商品详情查看
- 购物车管理
- 结算下单
- 订单查询

## 快速启动

### 环境要求

- JDK 21+
- Node.js 18+
- Maven 3.8+
- Redis（可选，H2 内置数据库开箱即用）

### 一键启动

**Windows：**
```bat
start.bat
```

**Linux / macOS：**
```bash
chmod +x start.sh stop.sh
./start.sh
```

### 手动启动

**1. 启动后端：**
```bash
cd backend
mvn spring-boot:run
```
后端运行在 `http://localhost:8081`。

**2. 启动前端：**
```bash
cd frontend
npm install
npm run dev
```
前端运行在 `http://localhost:5173`。

**3. 启动小程序（可选）：**
```bash
cd uniapp
npm install
# H5 开发
npm run dev:h5
# 微信小程序开发
npm run dev:mp-weixin
```

### 访问地址

| 入口 | 地址 |
|------|------|
| 后端 API | http://localhost:8081 |
| H2 数据库控制台 | http://localhost:8081/h2-console |
| 管理后台 | http://localhost:5173/dashboard |
| 用户端 | http://localhost:5173/client/index |

## 默认配置

- 数据库：H2 内存数据库（开发环境），连接信息见 `backend/src/main/resources/application.yml`
- JWT 密钥在 `application.yml` 中配置，生产环境请修改为安全密钥
- 端口：后端 `8081`，前端 `5173`
