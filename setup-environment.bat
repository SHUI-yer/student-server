@echo off
echo Starting Environment Setup...
echo.
echo IMPORTANT: This script requires Administrator privileges.
echo If prompted, click "Yes" to allow changes.
echo.
pause
powershell -ExecutionPolicy Bypass -File "%~dp0setup-environment.ps1"
pause
