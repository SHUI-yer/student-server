# ☕ 学生信息管理系统 - 后端服务 (student-server)

> 一个基于 **Java 17 + Spring Boot 4.0.6 + MyBatis + JWT + EasyExcel** 的企业级全栈后端服务。
> 项目深度集成了数字化统计、多维分析算法、文件持久化及高性能 Excel 批量处理能力。

---

# 🔗 相关仓库

| 仓库 | 地址 | 说明 |
|------|------|------|
| **前端** | [student_client](https://github.com/SHUI-yer/student_client) | Vue 3 + TypeScript 前端项目 |
| **后端** | [student-server](https://github.com/SHUI-yer/student-server) | Spring Boot 后端项目（本仓库） |

---

# 🚀 快速部署（给 Clone 用户）

### 环境要求
| 软件 | 版本 | 用途 |
|------|------|------|
| Java JDK | 17+ | 运行后端 |
| MySQL | 8.0+ | 数据库 |
| Node.js | 18+ | 运行前端（可选） |

### 一键环境检查（推荐）

运行环境检查脚本，自动检测并安装缺失的依赖：

```powershell
# Right-click PowerShell and run as Administrator
.\setup-environment.ps1
```

The script will:
- Check if Java JDK 17+, MySQL 8.0+, Node.js 18+ are installed
- Automatically download and install missing software using Chinese mirrors
- Configure npm to use Chinese mirror for faster downloads

### 一键部署步骤

```bash
# 1. Clone both repositories
git clone https://github.com/SHUI-yer/student-server.git
git clone https://github.com/SHUI-yer/student_client.git

# 2. Create database
mysql -u root -p -e "CREATE DATABASE student_systerm DEFAULT CHARACTER SET utf8mb4;"

# 3. Import data
mysql -u root -p student_systerm < student-server/init_database.sql

# 4. Configure database password
# Edit student-server/src/main/resources/application-local.properties
# Change spring.datasource.password=your_password

# 5. Start backend (Terminal 1)
cd student-server
./mvnw spring-boot:run

# 6. Start frontend (Terminal 2)
cd student_client
npm install
npm run dev

# 7. Access the system
# Open browser http://localhost:5173
```

### Default Login
| Username | Password |
|----------|----------|
| admin | 123456 |

---

# 🚀 核心技术特性 (Technical Highlights)

- **数字化分析引擎**: 通过 StatController 实时产出历年成绩趋势、及格率波动及基于**标准差 (SD)** 的课程难度评估模型。
- **EasyExcel 全能互操作**: 实现全业务模块（学生、课程、成绩）的流式导入导出。支持**按条件筛选导出**（如按专业、班级、学期过滤）。
- **学分制自动换算**: 核心逻辑引擎实现 `originalScore * credit / 100` 的自动折算，确保绩点统计的物理严谨性。
- **严谨数据校验**: 集成 Hibernate Validator，在 Controller 层通过 `@Validated` 强制拦截非法参数（年龄限制 1-120，成绩限制 0-100）。
- **全栈文件存储**: 支持头像图片的异步上传、UUID 随机重命名防冲突、以及静态资源放行映射。
- **三位一体安全体系**: 基于 JWT 的请求拦截、全局 Result 封装、以及 RESTful 异常统一处理。
- **前端静态资源托管**: 支持将 Vue 打包产物嵌入 jar，实现单文件部署。

---

# 📂 核心目录结构
```text
student-server/
├── src/main/java/com/suiye/studentserver/
│   ├── common/             # 统一响应封装 (Result<T>)
│   ├── config/             # JWT、CORS、静态资源及前端托管配置
│   ├── controller/         # RESTful 接口 (含统计分析、Excel 接口)
│   ├── entity/             # 实体类 (含 EasyExcel 注解与 Validation 约束)
│   ├── exception/          # 全局异常拦截器
│   ├── interceptor/        # JWT 安全通行证拦截
│   ├── mapper/             # MyBatis 接口与 SQL 映射
│   └── service/            # 业务层 (含成绩折算算法、统计聚合逻辑)
├── src/main/resources/
│   ├── application.properties        # 主配置文件
│   ├── application-local.properties  # 本地配置 (数据库连接等)
│   └── static/                       # 前端打包产物 (构建时自动生成)
├── init_database.sql       # 🏦 标准化数据库初始化脚本
├── uploads/                # 📁 头像文件持久化目录
└── pom.xml                 # Maven 项目配置
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

# 🛠️ 环境准备

### 系统要求
| 环境 | 版本要求 | 说明 |
|------|----------|------|
| **Java JDK** | 17+ | 推荐 Zulu JDK 或 Oracle JDK |
| **Maven** | 3.9+ | 或使用项目自带的 `mvnw` |
| **MySQL** | 8.0+ | 需提前安装并启动服务 |

### 数据库配置

1. **创建数据库**
```sql
CREATE DATABASE student_systerm DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **导入初始化脚本**
```bash
mysql -u root -p student_systerm < init_database.sql
```

3. **配置数据库连接**

编辑 `src/main/resources/application-local.properties`：
```properties
# 数据库连接配置
spring.datasource.url=jdbc:mysql://localhost:3306/student_systerm?serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=你的密码

# 服务器端口
server.port=8080

# MyBatis 配置
mybatis.configuration.map-underscore-to-camel-case=true
```

---

# 🚀 启动方式

### 方式一：IDE 启动（推荐开发时使用）

1. 使用 IDEA 或 Eclipse 打开项目
2. 等待 Maven 依赖下载完成
3. 运行 `StudentServerApplication.java` 主类

### 方式二：命令行启动

```bash
# Windows 环境
mvnw.cmd clean spring-boot:run

# 或者先打包再运行
mvnw.cmd clean package -DskipTests
java -jar target/student-server-0.0.1-SNAPSHOT.jar
```

### 方式三：一体化部署（包含前端）

```bash
# 在项目根目录运行构建脚本
.\build_all.bat

# 进入 release 目录
cd release

# 启动服务
java -jar app.jar
# 或双击 run_server.bat
```

启动后访问：**http://localhost:8080**

---

# 🔐 默认登录账号

| 用户名 | 密码 |
|--------|------|
| admin | 123456 |

---

# 📝 配置说明

### 端口修改
在 `application-local.properties` 中修改：
```properties
server.port=8080  # 修改为其他端口
```

### 文件上传路径
默认上传到项目运行目录下的 `uploads/` 文件夹，可在 `WebMvcConfig.java` 中修改。

### JWT 密钥
当前密钥硬编码在代码中（演示用途），生产环境建议改为环境变量。

---

# 💎 开发环境依赖
- **Java SDK**: 17+ (推荐 Zulu JDK)
- **Maven**: 3.9+
- **MySQL**: 8.0+

---

# 📚 相关文档
- [数据库初始化脚本](init_database.sql)
- [前端项目](https://github.com/SHUI-yer/student_client)
