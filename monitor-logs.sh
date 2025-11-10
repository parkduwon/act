#!/bin/bash

echo "ACT Backend 로그 모니터링 시작... (Ctrl+C로 종료)"
echo "=========================================="

sshpass -p 'c205' ssh -o StrictHostKeyChecking=no -p 2222 smith@125.131.198.22 "docker exec act-backend tail -f /app/logs/act-application.log"