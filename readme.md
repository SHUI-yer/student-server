# ☕ 学生信息管理系统 - 后端服务 (student-server)

本项目是学生信息管理系统的后端核心，基于 Java 25 和 Spring Boot 4.0.6 架构设计，采用 JWT 机制进行身份加密与认证。

---

## 🛠️ 一、 本地开发环境依赖

在运行本项目前，请确保您的本地电脑已配置以下环境：

* **Java SDK**: `Azul Zulu 25.0.3` 或更高版本 (兼容 JDK 25+)
* **构建工具**: `Maven 3.9+`
* **数据库**: `MySQL 8.0` 或更高版本
* **推荐 IDE**: `IntelliJ IDEA Ultimate / Community Edition`

---

## 🚀 二、 克隆后的快速配置与运行

### 步骤 1：初始化数据库

1. 打开您的 MySQL 数据库管理工具（如 DataGrip / Navicat）。
2. 新建一个数据库，命名为：**`student_systerm`**（字符集推荐：`utf8mb4`）。
3. 执行本项目根目录下 `sql/` 目录中的建表脚本（或导入对应的 DDL 语句），确保 `user`、`student` 等初始核心表及测试数据生成完毕。

### 步骤 2：修改数据库本地连接配置

由于敏感信息隔离，代码中的数据库密码已作占位处理。

1. 打开 `src/main/resources/application.properties` 文件。

2. 找到以下配置，将密码修改为您**本地真实的 MySQL 密码**：

   ```properties
   spring.datasource.username=root
   spring.datasource.password=填写您本地的数据库密码
   ```

###                       步骤 3：启动服务

- **方法 A（IDE 一键启动）**：用 IntelliJ IDEA 打开本工程，等待 Maven 依赖下载完毕后，找到主启动类 `StudentServerApplication.java`，点击 **绿色三角按钮 (Run)** 启动。
- **方法 B（命令行启动）**：在工程根目录下打开终端，执行以下命令：

Bash

```
mvn clean spring-boot:run
```

当控制台打印出如下标志，证明后端服务在 **`8080`** 端口通电成功：

Plaintext

```
Tomcat started on port 8080 (http) with context path '/'
Started StudentServerApplication in ...
```

------

## 🔒 三、 初始内置测试账号

本系统已对跨域请求（CORS）进行了全局/控制层放行，允许前端端口进行安全联调：

- **管理员账号**：`admin`
- **初始密码**：`123456`

*(注：登录成功后，后端将颁发标准的签名 Token，前端需在请求拦截器中携带 `Authorization: Bearer <Token>` 即可正常通行。)*