#!/bin/bash
echo "=== 启动电商平台服务 ==="

# 停止已运行的服务
echo "检查并停止已有服务..."
if [ -f "backend.pid" ]; then
    kill -9 $(cat backend.pid) 2>/dev/null || true
    rm -f backend.pid
fi
if [ -f "frontend.pid" ]; then
    kill -9 $(cat frontend.pid) 2>/dev/null || true
    rm -f frontend.pid
fi

# 启动后端服务
echo "启动后端服务..."
cd backend
nohup mvn spring-boot:run > ../backend.log 2>&1 &
echo $! > ../backend.pid
cd ..

# 等待后端启动
echo "等待后端服务启动..."
sleep 10

# 启动前端服务
echo "启动前端服务..."
cd frontend
nohup npm run dev > ../frontend.log 2>&1 &
echo $! > ../frontend.pid
cd ..

echo ""
echo "=== 服务启动完成 ==="
echo "📦 后端服务: http://localhost:8081"
echo "🌐 前端服务: http://localhost:5173"
echo "🛒 用户端: http://localhost:5173/client/index"
echo "🔧 管理后台: http://localhost:5173/dashboard"
echo ""
echo "日志文件:"
echo "  - 后端日志: backend.log"
echo "  - 前端日志: frontend.log"
echo ""
echo "停止服务: ./stop.sh"