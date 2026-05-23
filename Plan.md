# 📚 学生信息管理系统 - 全栈开发每日敏捷计划表（Plan.md）

> 一个基于 **Spring Boot + Vue 3 + TypeScript + Element Plus** 的前后端分离学生信息管理系统开发路线图。
> 本文档用于记录项目每日研发进度、阶段性成果与后续攻坚方向。

---

# 🛠️ 第一周：打牢地基（5月17日 - 5月23日）

> 目标：完成项目基础架构、登录认证与前后端安全通信闭环。

## ✅ Day 1（5月17日）

### 数据库初始化 & 登录接口打通

* [x] 在 DataGrip 中运行 DDL 脚本初始化 `student_systerm`
* [x] 使用 IDEA 构建 Spring Boot 项目骨架
* [x] 编写 `POST /api/login` 登录接口
* [x] 集成 JWT 生成 Token
* [x] 使用 Apifox 联调成功获取 Token

### 📌 技术关键词

`Spring Boot` `MySQL` `MyBatis` `JWT` `Apifox`

---

## ✅ Day 2（5月18日）

### Vue3 工程初始化 & 登录页开发

* [x] 使用 Vite 初始化 Vue3 + TypeScript 项目
* [x] 封装 Axios 请求工具
* [x] 实现请求拦截器自动携带：

```http
Authorization: Bearer <Token>
```

* [x] 使用 Element Plus 完成统一视觉登录页
* [x] 完成前后端首次联调

### 📌 技术关键词

`Vue 3` `TypeScript` `Axios` `Element Plus` `Vite`

---

## ✅ Day 3（5月19日）

### 后端 JWT 安全拦截器

* [x] 编写 Spring Boot Interceptor
* [x] 拦截除登录外的非法请求
* [x] 校验 Token 是否：

    * 存在
    * 合法
    * 未过期
* [x] 非法请求统一返回：

```http
401 Unauthorized
```

### 📌 技术关键词

`Interceptor` `JWT Verify` `Spring MVC`

---

## ✅ Day 4（5月20日）

### 前端全局路由守卫 & 自动失效处理

* [x] 配置 Vue Router 全局前置守卫：

```ts
router.beforeEach()
```

* [x] 未登录用户强制重定向 `/login`
* [x] Axios 响应拦截器统一捕获 `401`
* [x] 自动清除本地缓存 Token
* [x] 自动跳转登录页并提示异常

### 📌 技术关键词

`Vue Router` `Navigation Guard` `Axios Interceptor`

---

## ✅ Day 5（5月21日）

### 后台 Layout 主框架搭建

* [x] 使用 `el-container` 搭建后台布局
* [x] 实现：

    * 左侧菜单栏
    * 顶部导航栏
    * 主内容区域
* [x] 打通菜单与路由联动

### 📌 技术关键词

`Layout` `Element Plus` `Router View`

---

## ✅ Day 6 - Day 7（5月22日 - 5月23日）

### 第一周阶段性重构与测试

* [x] 全盘接口联调测试
* [x] 防呆测试与异常处理
* [x] 代码结构优化
* [x] 清理冗余代码
* [x] 补充注释与规范命名

---

# 🚀 第二周：核心业务攻坚（5月24日 - 5月30日）

> 目标：完成学生、课程、成绩三大核心业务模块。

---

## ✅ Day 8（5月24日）

### 学生管理模块（后端）

* [x] 编写 Student 实体与 Mapper
* [x] 实现分页查询
* [x] 实现模糊搜索
* [x] 完成 RESTful CRUD 接口

### 📌 技术关键词

`RESTful API` `Page Query` `MyBatis`

---

## ✅ Day 9（5月25日）

### 学生管理模块（前端列表）

* [x] 使用 `el-table` 渲染动态数据
* [x] 使用 `el-pagination` 实现分页
* [x] 完成条件搜索联动

### 📌 技术关键词

`el-table` `Pagination`

---

## ✅ Day 10（5月26日）

### 学生管理模块（前端表单）

* [x] 使用 `el-dialog` 实现弹窗表单
* [x] 合并新增 / 编辑逻辑
* [x] 实现表单数据回显
* [x] 增加危险操作确认弹窗

### 📌 技术关键词

`Dialog` `Form Validation`

---

## ✅ Day 11 - Day 12（5月27日 - 5月28日）

### 课程管理模块快速复用

* [x] 基于学生模块快速复制课程模块
* [x] 实现 Course CRUD
* [x] 前后端整体联调完成

### 📌 技术关键词

`代码复用` `模块抽象`

---

## ✅ Day 13（5月29日）

### 成绩查询模块（后端高级 SQL）

* [x] 编写三表 JOIN 联查：

    * `score`
    * `student`
    * `course`

* [x] 支持：

    * 学生 ID 检索
    * 课程 ID 检索
    * 组合条件查询

### 📌 技术关键词

`SQL JOIN` `复杂查询`

---

## ✅ Day 14（5月30日）

### 成绩查询模块（前端联动）

* [x] 使用 `el-select` 实现双下拉联动
* [x] 动态同步学生与课程
* [x] 表格展示交叉成绩单

### 📌 技术关键词

`动态筛选` `联动查询`

---

# 🎨 第三周：优化、封装与答辩准备（5月31日 - 6月6日）

> 目标：完善用户体验、补全文档、准备项目验收。

---

## ⏳ Day 15 - Day 16

### 图片上传（挑战功能）

* [ ] 集成 `el-upload`
* [ ] 使用 `FormData`
* [ ] 打通头像上传业务流

### 📌 技术关键词

`Multipart` `File Upload`

---

## ⏳ Day 17 - Day 18

### 全局异常优化

* [ ] 配置前端 404 路由
* [ ] 按钮增加 `loading`
* [ ] 优化前后端异常提示
* [ ] 防止重复提交与脏数据

### 📌 技术关键词

`UX` `404` `Loading`

---

## ⏳ Day 19 - Day 20

### 文档与答辩准备

* [ ] 完善 README
* [ ] 编写本地运行指南
* [ ] 整理踩坑笔记
* [ ] 提炼项目亮点
* [ ] 制作答辩 PPT

### 📌 技术关键词

`Documentation` `Presentation`

---

## 🎯 Day 21（6月6日）

# 项目验收日

* [ ] 10 分钟完整项目演示
* [ ] 功能全流程跑通
* [ ] 展示安全拦截链路
* [ ] 展示分页、搜索、联动查询
* [ ] 高分通过验收 🎉

---

# 🧩 当前项目技术栈总览

| 分类   | 技术                      |
| ---- | ----------------------- |
| 后端   | Spring Boot、MyBatis、JWT |
| 前端   | Vue 3、TypeScript、Vite   |
| UI   | Element Plus            |
| 数据库  | MySQL                   |
| 接口测试 | Apifox                  |
| 版本管理 | Git + GitHub            |
| 开发工具 | IDEA、WebStorm、DataGrip  |

---

# 📌 项目阶段总结

目前项目已完成：

* ✅ 登录鉴权闭环
* ✅ JWT 双重安全防线
* ✅ 学生管理 CRUD
* ✅ 课程管理 CRUD
* ✅ 成绩联表查询
* ✅ 前后端完整联调
* ✅ 企业级后台基础架构

项目整体已进入：

```text
功能完善 + 用户体验优化 + 答辩冲刺阶段
```
