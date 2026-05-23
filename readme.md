# ☕ 学生信息管理系统 - 后端服务（student-server）

> 一个基于 **Java 25 + Spring Boot 4.0.6 + MyBatis + JWT** 的企业级学生信息管理系统后端服务。
> 项目采用前后端分离架构，提供统一 RESTful API，并基于 JWT 实现身份认证与安全拦截。

---

# 🛠️ 一、本地开发环境依赖

在运行本项目前，请确保本地环境已正确安装以下工具：

| 环境       | 推荐版本                               |
| -------- | ---------------------------------- |
| Java SDK | Azul Zulu `25.0.3+`                |
| Maven    | `3.9+`                             |
| MySQL    | `8.0+`                             |
| IDE      | IntelliJ IDEA Ultimate / Community |

---

# 🚀 二、快速启动指南

---

## 📌 步骤 1：初始化数据库

### 1. 创建数据库

打开 MySQL 管理工具（DataGrip / Navicat / MySQL Workbench），创建数据库：

```sql
CREATE DATABASE student_systerm
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

---

### 2. 导入初始化 SQL

执行项目根目录下的：

```text
init_database.sql
```

初始化完成后，应生成以下核心业务表：

* `student`
* `course`
* `score`
* `user`

以及对应的外键关联与级联约束。

---

# ⚙️ 三、本地数据库配置

由于安全原因，真实数据库密码未上传至 GitHub。

请自行创建本地配置文件：

```text
src/main/resources/application-local.properties
```

并填写如下内容：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_systerm?serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf-8

spring.datasource.username=root
spring.datasource.password=填写您本地的MySQL密码

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

server.port=8080

mybatis.configuration.map-underscore-to-camel-case=true
```

---

# ▶️ 四、启动项目

---

## 方法 A：IDEA 一键启动（推荐）

1. 使用 IntelliJ IDEA 打开项目
2. 等待 Maven 自动下载依赖
3. 找到启动类：

```text
StudentServerApplication.java
```

4. 点击绿色运行按钮 ▶️

---

## 方法 B：命令行启动

在项目根目录执行：

```bash
mvn clean spring-boot:run
```

如果项目使用了 Maven Wrapper（推荐）：

Linux / macOS：

```bash
./mvnw spring-boot:run
```

Windows：

```bash
mvnw.cmd spring-boot:run
```

---

## ✅ 启动成功标志

控制台输出如下内容即代表启动成功：

```text
Tomcat started on port 8080 (http)
Started StudentServerApplication
```

后端服务默认运行于：

```text
http://localhost:8080
```

---

# 🔒 五、初始测试账号

系统已默认放行前端跨域请求（CORS），可直接进行联调测试。

| 类型    | 内容       |
| ----- | -------- |
| 管理员账号 | `admin`  |
| 初始密码  | `123456` |

---

## 📌 登录成功后

后端会返回 JWT Token：

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "token": "xxxxxxxx"
  }
}
```

前端需在请求头中携带：

```http
Authorization: Bearer <Token>
```

---

# 🧩 六、项目核心架构（Core Architecture）

项目遵循标准 Spring Boot 分层架构，实现高内聚、低耦合设计。

```text
src/main/java/com/suiye/studentserver/
├── controller/     # 控制层（接口入口）
├── service/        # 业务逻辑层
├── mapper/         # MyBatis 持久层
├── entity/         # 实体类
├── interceptor/    # JWT 安全拦截器
├── common/         # 通用响应封装
├── exception/      # 全局异常处理
└── config/         # Spring MVC 配置
```

---

## 📌 核心模块说明

### `controller`

负责接收 HTTP 请求并返回统一响应。

主要包含：

* `LoginController`
* `StudentController`
* `CourseController`
* `ScoreController`

---

### `service`

封装核心业务逻辑：

* 分页处理
* 数据校验
* 条件查询
* 数据转换

---

### `mapper`

基于 MyBatis 实现 SQL 持久化操作。

---

### `interceptor`

JWT 登录鉴权核心保安。

功能包括：

* Token 验签
* Token 过期校验
* 非法请求拦截
* 返回 `401 Unauthorized`

---

# 📊 七、已完成 API 概览

---

## 🔐 1. 登录认证模块

| 请求方式 | 接口           |
| ---- | ------------ |
| POST | `/api/login` |

### 返回类型

```java
Result<Map<String, String>>
```

---

## 👨‍🎓 2. 学生管理模块

| 请求方式   | 接口                  | 说明      |
| ------ | ------------------- | ------- |
| GET    | `/api/student/page` | 分页查询    |
| POST   | `/api/student/save` | 新增 / 修改 |
| DELETE | `/api/student/{id}` | 删除学生    |

---

## 📚 3. 课程管理模块

| 请求方式   | 接口                 | 说明      |
| ------ | ------------------ | ------- |
| GET    | `/api/course/page` | 分页查询    |
| POST   | `/api/course/save` | 新增 / 修改 |
| DELETE | `/api/course/{id}` | 删除课程    |

---

## 📈 4. 成绩查询模块

| 请求方式 | 接口                | 说明      |
| ---- | ----------------- | ------- |
| GET  | `/api/score/list` | 多条件成绩查询 |

支持：

* 学生 ID 查询
* 课程 ID 查询
* 三表 JOIN 联查

---

# 🛡️ 八、安全机制说明

项目采用“双重安全防线”。

---

## 前端防线

### Vue Router 路由守卫

```ts
router.beforeEach()
```

未登录用户禁止访问后台页面。

---

## 后端防线

### JWT 拦截器

```text
JwtInterceptor
```

负责：

* 校验 Token
* 验证签名
* 检查过期时间
* 拦截非法请求

---

# 💎 九、项目重构亮点

---

## ✅ 统一响应结构

统一返回：

```java
Result<T>
```

避免前后端字段混乱。

---

## ✅ 全局异常处理

统一异常捕获：

```text
GlobalExceptionHandler
```

实现：

* 参数异常处理
* Token 异常处理
* 系统异常兜底

---

## ✅ 前后端完全分离

技术栈独立运行：

| 模块             | 端口     |
| -------------- | ------ |
| 前端 Vue         | `5173` |
| 后端 Spring Boot | `8080` |

---

## ✅ 企业级 JWT 鉴权

实现：

* 登录颁发 Token
* 请求自动携带 Token
* 后端统一验签
* Token 失效自动踢回登录页

---

# 📦 十、项目运行建议

推荐配套运行：

| 模块 | 项目               |
| -- | ---------------- |
| 前端 | `student_client` |
| 后端 | `student-server` |

建议同时启动：

```text
5173 (Vue Frontend)
8080 (Spring Boot Backend)
```

---

# 📝 十一、开发建议

推荐后续扩展方向：

* 文件上传（头像）
* RBAC 权限管理
* Redis Token 缓存
* Swagger/OpenAPI 文档
* Docker 容器部署
* Nginx 反向代理
* Linux 云服务器部署

---

# 🎯 当前项目完成度

| 模块      | 状态 |
| ------- | -- |
| 登录认证    | ✅  |
| JWT 鉴权  | ✅  |
| 学生管理    | ✅  |
| 课程管理    | ✅  |
| 成绩查询    | ✅  |
| 前后端联调   | ✅  |
| 企业级后台框架 | ✅  |

---

# 🚀 项目当前阶段

```text
功能完善 + 用户体验优化 + 项目答辩冲刺阶段
```
