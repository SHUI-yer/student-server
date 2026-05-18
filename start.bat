@echo off
:: 解决乱码问题
chcp 65001
title 全栈项目一键启动脚本

echo ====================================================
echo  🚀 正在启动 [学生管理系统] 前后端服务，请稍候...
echo ====================================================

echo ⚙️ 1. 正在后台启动 Java 后端 (Spring Boot)...
cd /d D:\SchoolProject\student-server
:: 使用 mvn 重新编译并运行，这样你不用开 IDEA 也能跑后端
start "Java Backend (Port 8080)" cmd /k "mvn clean spring-boot:run"

echo 🎨 2. 正在后台启动 Vue3 前端 (Vite)...
cd /d D:\SchoolProject\student_client
:: 自动打开运行前端服务
start "Vue3 Frontend (Port 5173)" cmd /k "npm run dev"

echo ====================================================
echo  ✨ 前后端服务已成功拉起！
echo  - 后端服务运行于：http://localhost:8080
echo  - 前端服务运行于：http://localhost:5173
echo ====================================================
pause