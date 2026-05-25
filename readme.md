# ☕ 学生信息管理系统 - 后端服务 (student-server)

> 一个基于 **Java 17 + Spring Boot 4.0.6 + MyBatis + JWT + EasyExcel** 的企业级全栈后端服务。
> 项目深度集成了数字化统计、多维分析算法、文件持久化及高性能 Excel 批量处理能力。

---

# 🚀 核心技术特性 (Technical Highlights)

- **数字化分析引擎**: 通过 StatController 实时产出历年成绩趋势、及格率波动及基于**标准差 (SD)** 的课程难度评估模型。
- **EasyExcel 全能互操作**: 实现全业务模块（学生、课程、成绩）的流式导入导出。支持**按条件筛选导出**（如按专业、班级、学期过滤）。
- **学分制自动换算**: 核心逻辑引擎实现 `originalScore * credit / 100` 的自动折算，确保绩点统计的物理严谨性。
- **严谨数据校验**: 集成 Hibernate Validator，在 Controller 层通过 `@Validated` 强制拦截非法参数（年龄限制 1-120，成绩限制 0-100）。
- **全栈文件存储**: 支持头像图片的异步上传、UUID 随机重命名防冲突、以及静态资源放行映射。
- **三位一体安全体系**: 基于 JWT 的请求拦截、全局 Result 封装、以及 RESTful 异常统一处理。

---

# 📂 核心目录结构
```text
student-server/
├── src/main/java/com/suiye/studentserver/
│   ├── common/             # 统一响应封装 (Result<T>)
│   ├── config/             # JWT、CORS 及静态资源放行配置
│   ├── controller/         # RESTful 接口 (含统计分析、Excel 接口)
│   ├── entity/             # 实体类 (含 EasyExcel 注解与 Validation 约束)
│   ├── exception/          # 全局异常拦截器
│   ├── interceptor/        # JWT 安全通行证拦截
│   ├── mapper/             # MyBatis 接口与 SQL 映射
│   └── service/            # 业务层 (含成绩折算算法、统计聚合逻辑)
├── src/main/resources/
│   ├── application.properties
│   └── application-local.properties # 本地机密配置 (数据库密码)
├── init_database.sql       # 🏦 标准化数据库初始化脚本 (含最新原始分字段)
└── uploads/                # 📁 头像文件持久化保险箱
```

---

# 📊 接口进展看板 (API Status)
- [x] **认证接口**: 登录授权、Token 签发与生命周期管理
- [x] **学生管理**: 分页 CRUD、头像上传、Excel 导入导出
- [x] **课程管理**: 分页 CRUD、学分绑定、Excel 导入导出
- [x] **成绩系统**: 成绩折算保存、三表联查、Excel 导入导出
- [x] **决策统计**: 大屏实时聚合、趋势分析、难度雷达矩阵
- [x] **系统维护**: 全局异常拦截、物理数据校验拦截

---

# 🚀 极速启动指南

### 1. 数据库准备
1. 执行 `init_database.sql` 脚本，建立 `student`、`course`、`score` 和 `user` 表。

### 2. 本地配置
1. 在 `src/main/resources/` 下确保 `application-local.properties` 配置了正确的 MySQL 密码。

### 3. 编译运行
```bash
# Windows 环境
mvnw.cmd clean spring-boot:run
```

---

## 💎 开发环境依赖
- **Java SDK**: 17+ (推荐 Zulu JDK)
- **Maven**: 3.9+
- **MySQL**: 8.0+
