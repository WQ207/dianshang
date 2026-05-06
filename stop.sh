#!/bin/bash
echo "=== 停止电商平台服务 ==="

# 停止后端服务
if [ -f "backend.pid" ]; then
    echo "停止后端服务..."
    kill -9 $(cat backend.pid) 2>/dev/null || true
    rm -f backend.pid
    echo "✅ 后端服务已停止"
else
    echo "ℹ️ 后端服务未运行"
fi

# 停止前端服务
if [ -f "frontend.pid" ]; then
    echo "停止前端服务..."
    kill -9 $(cat frontend.pid) 2>/dev/null || true
    rm -f frontend.pid
    echo "✅ 前端服务已停止"
else
    echo "ℹ️ 前端服务未运行"
fi

echo ""
echo "=== 所有服务已停止 ==="