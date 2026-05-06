@echo off
chcp 65001
echo === 启动电商平台服务 ===

:: 启动后端服务
echo 启动后端服务...
start "backend" cmd /k "cd backend && mvn spring-boot:run"

:: 等待后端启动
echo 等待后端服务启动...
timeout /t 10 /nobreak > nul

:: 启动前端服务
echo 启动前端服务...
start "frontend" cmd /k "cd frontend && npm run dev"

echo.
echo === 服务启动完成 ===
echo 📦 后端服务: http://localhost:8081
echo 🌐 前端服务: http://localhost:5173
echo 🛒 用户端: http://localhost:5173/client/index
echo 🔧 管理后台: http://localhost:5173/dashboard
echo.
echo 按任意键退出...
pause > nul