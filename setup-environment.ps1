# Student Management System - Environment Setup Script
# This script checks and installs required dependencies

#Requires -RunAsAdministrator

param(
    [switch]$SkipMySQL,
    [switch]$SkipNode,
    [switch]$Force
)

$ErrorActionPreference = "Stop"

# ============================================
# Helper Functions
# ============================================

function Write-Status {
    param([string]$Message, [string]$Status, [string]$Color = "White")
    Write-Host "  [" -NoNewline
    Write-Host $Status -ForegroundColor $Color -NoNewline
    Write-Host "] $Message"
}

function Test-Command {
    param([string]$Command)
    try {
        $null = Get-Command $Command -ErrorAction Stop
        return $true
    } catch {
        return $false
    }
}

function Get-JavaVersion {
    try {
        $version = java -version 2>&1 | Select-String "version" | ForEach-Object { $_.ToString().Split('"')[1] }
        return $version
    } catch {
        return $null
    }
}

function Get-NodeVersion {
    try {
        $version = node --version 2>$null
        return $version
    } catch {
        return $null
    }
}

function Get-MySQLVersion {
    try {
        $version = mysql --version 2>$null
        if ($version -match "Distrib (\d+\.\d+\.\d+)") {
            return $Matches[1]
        }
        return $version
    } catch {
        return $null
    }
}

# ============================================
# Download Functions (Using Chinese Mirrors)
# ============================================

function Download-File {
    param(
        [string]$Url,
        [string]$Output
    )
    
    Write-Host "    Downloading from: $Url" -ForegroundColor Gray
    
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        $webClient = New-Object System.Net.WebClient
        $webClient.DownloadFile($Url, $Output)
        return $true
    } catch {
        Write-Host "    Download failed: $_" -ForegroundColor Red
        return $false
    }
}

function Install-Java {
    Write-Host ""
    Write-Host "Installing Java JDK 17..." -ForegroundColor Yellow
    
    # Use Tsinghua mirror for Eclipse Temurin
    $javaUrl = "https://mirrors.tuna.tsinghua.edu.cn/Adoptium/17/jdk/x64/windows/OpenJDK17U-jdk_x64_windows_hotspot_17.0.13_11.msi"
    $javaMsi = "$env:TEMP\java-jdk17.msi"
    
    Write-Host "  Downloading Java JDK 17 from Tsinghua mirror..."
    if (Download-File -Url $javaUrl -Output $javaMsi) {
        Write-Host "  Installing Java JDK 17 (this may take a few minutes)..."
        Start-Process msiexec.exe -ArgumentList "/i `"$javaMsi`" /quiet /norestart ADDLOCAL=FeatureMain,FeatureEnvironment,FeatureJarFileRunWith,FeatureJavaHome" -Wait -NoNewWindow
        
        # Refresh environment variables
        $env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path", "User")
        
        if (Test-Command "java") {
            Write-Status -Message "Java JDK 17 installed successfully" -Status "OK" -Color "Green"
            Remove-Item $javaMsi -Force -ErrorAction SilentlyContinue
            return $true
        } else {
            Write-Status -Message "Java installation may require system restart" -Status "WARN" -Color "Yellow"
            return $true
        }
    }
    
    Write-Status -Message "Failed to install Java" -Status "FAIL" -Color "Red"
    return $false
}

function Install-Node {
    Write-Host ""
    Write-Host "Installing Node.js LTS..." -ForegroundColor Yellow
    
    # Use npmmirror (Chinese mirror)
    $nodeUrl = "https://npmmirror.com/mirrors/node/v20.18.0/node-v20.18.0-x64.msi"
    $nodeMsi = "$env:TEMP\node-lts.msi"
    
    Write-Host "  Downloading Node.js LTS from npmmirror..."
    if (Download-File -Url $nodeUrl -Output $nodeMsi) {
        Write-Host "  Installing Node.js LTS..."
        Start-Process msiexec.exe -ArgumentList "/i `"$nodeMsi`" /quiet /norestart" -Wait -NoNewWindow
        
        # Refresh environment variables
        $env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path", "User")
        
        if (Test-Command "node") {
            Write-Status -Message "Node.js LTS installed successfully" -Status "OK" -Color "Green"
            Remove-Item $nodeMsi -Force -ErrorAction SilentlyContinue
            return $true
        } else {
            Write-Status -Message "Node.js installation may require system restart" -Status "WARN" -Color "Yellow"
            return $true
        }
    }
    
    Write-Status -Message "Failed to install Node.js" -Status "FAIL" -Color "Red"
    return $false
}

function Install-MySQL {
    Write-Host ""
    Write-Host "Installing MySQL 8.0..." -ForegroundColor Yellow
    
    # Use MySQL China mirror
    $mysqlUrl = "https://mirrors.tuna.tsinghua.edu.cn/mysql/downloads/MySQL-8.0/mysql-8.0.40-winx64.zip"
    $mysqlZip = "$env:TEMP\mysql.zip"
    $mysqlDir = "C:\MySQL"
    
    Write-Host "  Downloading MySQL 8.0 from Tsinghua mirror..."
    if (Download-File -Url $mysqlUrl -Output $mysqlZip) {
        Write-Host "  Extracting MySQL..."
        
        if (Test-Path $mysqlDir) {
            Remove-Item -Recurse -Force $mysqlDir
        }
        
        Expand-Archive -Path $mysqlZip -DestinationPath "C:\" -Force
        $extractedDir = Get-ChildItem -Path "C:\" -Directory | Where-Object { $_.Name -like "mysql-*" } | Select-Object -First 1
        
        if ($extractedDir) {
            Rename-Item $extractedDir.FullName $mysqlDir
        }
        
        # Add to PATH
        $currentPath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
        if ($currentPath -notlike "*$mysqlDir\bin*") {
            [System.Environment]::SetEnvironmentVariable("Path", "$currentPath;$mysqlDir\bin", "Machine")
            $env:Path += ";$mysqlDir\bin"
        }
        
        Write-Status -Message "MySQL extracted to $mysqlDir" -Status "OK" -Color "Green"
        Write-Host ""
        Write-Host "  IMPORTANT: MySQL requires manual configuration:" -ForegroundColor Yellow
        Write-Host "  1. Create my.ini configuration file" -ForegroundColor White
        Write-Host "  2. Initialize data directory: mysqld --initialize-insecure" -ForegroundColor White
        Write-Host "  3. Install as service: mysqld --install" -ForegroundColor White
        Write-Host "  4. Start service: net start mysql" -ForegroundColor White
        
        Remove-Item $mysqlZip -Force -ErrorAction SilentlyContinue
        return $true
    }
    
    Write-Status -Message "Failed to download MySQL" -Status "FAIL" -Color "Red"
    Write-Host ""
    Write-Host "  Please download MySQL manually from:" -ForegroundColor Yellow
    Write-Host "  https://dev.mysql.com/downloads/mysql/" -ForegroundColor Cyan
    return $false
}

# ============================================
# Main Script
# ============================================

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Student Management System - Environment Setup" -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""

$needsInstall = @()
$allGood = $true

# ============================================
# Check Java
# ============================================
Write-Host "Checking Java JDK..." -ForegroundColor Yellow

if (Test-Command "java") {
    $javaVer = Get-JavaVersion
    if ($javaVer -match "^17\.") {
        Write-Status -Message "Java JDK $javaVer found" -Status "OK" -Color "Green"
    } elseif ($javaVer -match "^(1[89]|[2-9]\d)\.") {
        Write-Status -Message "Java JDK $javaVer found (compatible)" -Status "OK" -Color "Green"
    } else {
        Write-Status -Message "Java JDK $javaVer found (version 17+ required)" -Status "WARN" -Color "Yellow"
        $needsInstall += "Java"
        $allGood = $false
    }
} else {
    Write-Status -Message "Java JDK not found" -Status "MISSING" -Color "Red"
    $needsInstall += "Java"
    $allGood = $false
}

# ============================================
# Check MySQL
# ============================================
if (-not $SkipMySQL) {
    Write-Host "Checking MySQL..." -ForegroundColor Yellow
    
    if (Test-Command "mysql") {
        $mysqlVer = Get-MySQLVersion
        Write-Status -Message "MySQL $mysqlVer found" -Status "OK" -Color "Green"
    } else {
        Write-Status -Message "MySQL not found (required for deployment)" -Status "WARN" -Color "Yellow"
        $needsInstall += "MySQL"
    }
}

# ============================================
# Check Node.js (Optional - for frontend development)
# ============================================
if (-not $SkipNode) {
    Write-Host "Checking Node.js..." -ForegroundColor Yellow
    
    if (Test-Command "node") {
        $nodeVer = Get-NodeVersion
        Write-Status -Message "Node.js $nodeVer found" -Status "OK" -Color "Green"
    } else {
        Write-Status -Message "Node.js not found (needed for frontend)" -Status "WARN" -Color "Yellow"
        $needsInstall += "Node.js"
    }
}

# ============================================
# Install Missing Dependencies
# ============================================
Write-Host ""

if ($needsInstall.Count -gt 0) {
    Write-Host "===================================================" -ForegroundColor Yellow
    Write-Host "  Missing Dependencies" -ForegroundColor Yellow
    Write-Host "===================================================" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "The following software needs to be installed:" -ForegroundColor White
    Write-Host ""
    
    foreach ($item in $needsInstall) {
        Write-Host "  - $item" -ForegroundColor Cyan
    }
    
    Write-Host ""
    Write-Host "Would you like to install them automatically?" -ForegroundColor White
    Write-Host ""
    Write-Host "  [Y] Yes - Install all missing software" -ForegroundColor Green
    Write-Host "  [N] No - I will install manually" -ForegroundColor Red
    Write-Host ""
    
    if ($Force -or (Read-Host "Enter choice (Y/N)") -eq "Y") {
        foreach ($item in $needsInstall) {
            switch ($item) {
                "Java" { Install-Java }
                "Node.js" { Install-Node }
                "MySQL" { Install-MySQL }
            }
        }
    } else {
        Write-Host ""
        Write-Host "Please install the missing software manually:" -ForegroundColor Yellow
        Write-Host ""
        if ("Java" -in $needsInstall) {
            Write-Host "  Java JDK 17:" -ForegroundColor White
            Write-Host "    https://adoptium.net/" -ForegroundColor Cyan
        }
        if ("Node.js" -in $needsInstall) {
            Write-Host "  Node.js LTS:" -ForegroundColor White
            Write-Host "    https://nodejs.org/" -ForegroundColor Cyan
        }
        if ("MySQL" -in $needsInstall) {
            Write-Host "  MySQL 8.0:" -ForegroundColor White
            Write-Host "    https://dev.mysql.com/downloads/mysql/" -ForegroundColor Cyan
        }
    }
} else {
    Write-Host "===================================================" -ForegroundColor Green
    Write-Host "  All dependencies are installed!" -ForegroundColor Green
    Write-Host "===================================================" -ForegroundColor Green
}

# ============================================
# Configure npm mirror (if Node.js is installed)
# ============================================
if (Test-Command "npm") {
    Write-Host ""
    Write-Host "Configuring npm to use Chinese mirror..." -ForegroundColor Yellow
    
    $currentRegistry = npm config get registry 2>$null
    if ($currentRegistry -notlike "*npmmirror*") {
        npm config set registry https://registry.npmmirror.com
        Write-Status -Message "npm registry set to npmmirror.com" -Status "OK" -Color "Green"
    } else {
        Write-Status -Message "npm already using Chinese mirror" -Status "OK" -Color "Green"
    }
}

# ============================================
# Summary
# ============================================
Write-Host ""
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Setup Complete!" -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next steps:" -ForegroundColor White
Write-Host "  1. Run build_all.bat or build_all.ps1 to build the project" -ForegroundColor Gray
Write-Host "  2. Or start development with: mvnw spring-boot:run" -ForegroundColor Gray
Write-Host ""
