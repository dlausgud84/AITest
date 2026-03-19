@echo off
title AiCodeTest MES Dev Server

cd /d "%~dp0"

echo.
echo ============================================
echo  AiCodeTest MES Dev Server
echo ============================================
echo.

echo Stopping processes on port 8080 and 3000...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8080 " ^| findstr LISTENING') do (
    taskkill /F /PID %%a > nul 2>&1
)
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":3000 " ^| findstr LISTENING') do (
    taskkill /F /PID %%a > nul 2>&1
)
timeout /t 2 /nobreak > nul

echo [1/2] Backend  : http://localhost:8080
start "Backend  - Spring Boot 8080" cmd /k "cd backend && gradlew.bat :apps:app:bootRun"

timeout /t 3 /nobreak > nul

echo [2/2] Frontend : http://localhost:3000
start "Frontend - Nuxt 3000" cmd /k "cd frontend\noroo-mes-app && npm run dev"

echo.
echo ============================================
echo  Ready =^> http://localhost:3000
echo ============================================
echo.
pause
