@echo off
cd /d "%~dp0"

echo Stopping existing dev server instances...
powershell -NoProfile -Command "$t=@('AiCodeTest MES Dev Server','Backend  - Spring Boot 8080','Frontend - Nuxt 3000'); Get-Process -Name cmd -ErrorAction SilentlyContinue | Where-Object { $t -contains $_.MainWindowTitle } | ForEach-Object { taskkill /F /T /PID $_.Id 2>$null }"
timeout /t 1 /nobreak > nul

title AiCodeTest MES Dev Server

echo.
echo ============================================
echo  AiCodeTest MES Dev Server
echo ============================================
echo.

echo Stopping processes on port 8080 and 3000...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8080 " ^| findstr LISTENING') do (
    taskkill /F /T /PID %%a > nul 2>&1
)
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":3000 " ^| findstr LISTENING') do (
    taskkill /F /T /PID %%a > nul 2>&1
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
