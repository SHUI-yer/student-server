# Student Management System - Build Script (PowerShell)
# This script is for student-server repository
# Frontend repo should be cloned to ../student_client

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Student Management System - Build Script" -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""

$BACKEND_DIR = $PSScriptRoot
$PROJECT_ROOT = Split-Path $BACKEND_DIR
$FRONTEND_DIR = Join-Path $PROJECT_ROOT "student_client"
$DIST_DIR = Join-Path $BACKEND_DIR "release"
$STATIC_DIR = Join-Path $BACKEND_DIR "src\main\resources\static"

# Check frontend directory
if (-not (Test-Path $FRONTEND_DIR)) {
    Write-Host "===================================================" -ForegroundColor Red
    Write-Host "  Frontend directory not found!" -ForegroundColor Red
    Write-Host "===================================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please clone the frontend repository to:" -ForegroundColor Yellow
    Write-Host "  $FRONTEND_DIR" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Run this command in the parent directory:" -ForegroundColor Yellow
    Write-Host "  git clone <frontend-repo-url> student_client" -ForegroundColor Cyan
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

# Check prerequisites
Write-Host "[0/4] Checking prerequisites..." -ForegroundColor Yellow
Write-Host ""

$hasError = $false

# Check Node.js
Write-Host "Checking Node.js..." -NoNewline
try {
    $nodeVer = node --version 2>$null
    Write-Host "  [OK] $nodeVer" -ForegroundColor Green
} catch {
    Write-Host "  [MISSING]" -ForegroundColor Red
    $hasError = $true
}

# Check npm
Write-Host "Checking npm..." -NoNewline
try {
    $npmVer = npm --version 2>$null
    Write-Host "  [OK] $npmVer" -ForegroundColor Green
} catch {
    Write-Host "  [MISSING]" -ForegroundColor Red
    $hasError = $true
}

# Check Java
Write-Host "Checking Java..." -NoNewline
try {
    $javaVer = java -version 2>&1 | Select-String "version" | ForEach-Object { $_.ToString().Split('"')[1] }
    Write-Host "  [OK] Java $javaVer" -ForegroundColor Green
} catch {
    Write-Host "  [MISSING]" -ForegroundColor Red
    $hasError = $true
}

# Check MySQL
Write-Host "Checking MySQL..." -NoNewline
try {
    $mysqlVer = mysql --version 2>$null
    Write-Host "  [OK]" -ForegroundColor Green
} catch {
    Write-Host "  [WARNING] Not found (H2 mode available)" -ForegroundColor Yellow
}

Write-Host ""

if ($hasError) {
    Write-Host "===================================================" -ForegroundColor Red
    Write-Host "  Some prerequisites are missing" -ForegroundColor Red
    Write-Host "===================================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install the missing software and try again."
    Read-Host "Press Enter to exit"
    exit 1
}

# Start build
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Starting build process..." -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""

# Cleanup
if (Test-Path $DIST_DIR) { Remove-Item -Recurse -Force $DIST_DIR }
New-Item -ItemType Directory -Path $DIST_DIR | Out-Null
New-Item -ItemType Directory -Path (Join-Path $DIST_DIR "uploads") | Out-Null

if (Test-Path $STATIC_DIR) { Remove-Item -Recurse -Force $STATIC_DIR }
New-Item -ItemType Directory -Path $STATIC_DIR | Out-Null

# Build frontend
Write-Host "[1/4] Building Frontend..." -ForegroundColor Yellow
Write-Host ""

Set-Location $FRONTEND_DIR
Write-Host "Current directory: $PWD" -ForegroundColor Gray
Write-Host ""

if (-not (Test-Path "node_modules")) {
    Write-Host "Installing npm dependencies..."
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERROR] npm install failed!" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}

Write-Host "Building frontend..."
npm run build
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Frontend build failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Copying frontend to backend..."
Copy-Item -Path "dist\*" -Destination $STATIC_DIR -Recurse -Force
Write-Host "[OK] Frontend build complete." -ForegroundColor Green
Write-Host ""

# Build backend
Write-Host "[2/4] Building Backend..." -ForegroundColor Yellow
Write-Host ""

Set-Location $BACKEND_DIR

.\mvnw clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Backend build failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

$jarFile = Get-ChildItem -Path "target\*.jar" | Where-Object { $_.Name -notmatch "original" } | Select-Object -First 1
if ($jarFile) {
    Copy-Item -Path $jarFile.FullName -Destination (Join-Path $DIST_DIR "app.jar") -Force
}
Write-Host "[OK] Backend build complete." -ForegroundColor Green
Write-Host ""

# Copy files
Write-Host "[3/4] Copying files..." -ForegroundColor Yellow
Copy-Item -Path (Join-Path $BACKEND_DIR "init_database.sql") -Destination $DIST_DIR -Force
Write-Host "[OK] Files copied." -ForegroundColor Green
Write-Host ""

# Create config and scripts
Write-Host "[4/4] Creating config and scripts..." -ForegroundColor Yellow

# Generate application-local.properties
$configContent = @'
# ==================================================
# Student Management System - Configuration
# ==================================================
# Edit this file to configure your environment

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/student_systerm?serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Server Port
server.port=8080

# MyBatis Configuration
mybatis.configuration.map-underscore-to-camel-case=true
'@

$configContent | Out-File -FilePath (Join-Path $DIST_DIR "application-local.properties") -Encoding UTF8

# Generate run_server.bat (MySQL mode)
$runScript = @'
@echo off
title Student Management System
echo ===================================================
echo   Student Management System
echo ===================================================
echo.
echo Please ensure MySQL is running.
echo Import database: mysql -u root -p ^< init_database.sql
echo.
echo Starting server on http://localhost:8080
echo Press Ctrl+C to stop.
echo ===================================================
echo.
java -jar app.jar
pause
'@

$runScript | Out-File -FilePath (Join-Path $DIST_DIR "run_server.bat") -Encoding ASCII

# Generate run_server_h2.bat (H2 mode - no MySQL needed)
$runH2Script = @'
@echo off
title Student Management System (H2 Mode)
echo ===================================================
echo   Student Management System (H2 Mode)
echo ===================================================
echo.
echo Using H2 in-memory database (no MySQL required)
echo H2 Console: http://localhost:8080/h2-console
echo.
echo Starting server on http://localhost:8080
echo Press Ctrl+C to stop.
echo ===================================================
echo.
java -jar app.jar --spring.profiles.active=h2
pause
'@

$runH2Script | Out-File -FilePath (Join-Path $DIST_DIR "run_server_h2.bat") -Encoding ASCII

# Generate start.bat
$startScript = @'
@echo off
title Starting Student Management System
echo Starting Student Management System in background...
start "Student Management System" /MIN java -jar app.jar
echo Server started! Access at http://localhost:8080
timeout /t 3 >nul
start http://localhost:8080
'@

$startScript | Out-File -FilePath (Join-Path $DIST_DIR "start.bat") -Encoding ASCII

# Generate stop.bat
$stopScript = @'
@echo off
title Stop Student Management System
echo ===================================================
echo   Stopping Student Management System...
echo ===================================================
echo.

echo Searching for Java process running app.jar...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
    echo Found process %%a, terminating...
    taskkill /PID %%a /F
)

echo.
echo Server stopped.
pause
'@

$stopScript | Out-File -FilePath (Join-Path $DIST_DIR "stop.bat") -Encoding ASCII

Write-Host "[OK] Config and scripts created." -ForegroundColor Green
Write-Host ""

# Done
Write-Host "===================================================" -ForegroundColor Green
Write-Host "  Build Complete!" -ForegroundColor Green
Write-Host "===================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Output directory: $DIST_DIR" -ForegroundColor Cyan
Write-Host ""
Write-Host "Files in release:" -ForegroundColor Yellow
Get-ChildItem $DIST_DIR | ForEach-Object { Write-Host "  - $($_.Name)" }
Write-Host ""
Write-Host "Quick Start:" -ForegroundColor Yellow
Write-Host "  1. Edit application-local.properties (MySQL mode)" -ForegroundColor White
Write-Host "  2. Run run_server.bat (MySQL mode)" -ForegroundColor White
Write-Host "  3. Or run run_server_h2.bat (H2 mode, no MySQL needed)" -ForegroundColor White
Write-Host ""

Set-Location $BACKEND_DIR
Read-Host "Press Enter to exit"
