#!/bin/bash

echo "====================================="
echo "   ACT Backend 실시간 로그 모니터링"
echo "====================================="
echo ""
echo "모니터링 옵션:"
echo "1) Docker 로그 (실시간)"
echo "2) 애플리케이션 로그 (거래 정보)"
echo "3) 에러 로그"
echo "4) 모든 로그 (병렬)"
echo ""
read -p "선택 (1-4): " choice

case $choice in
  1)
    echo "Docker 로그 모니터링 (Ctrl+C로 종료)..."
    ssh -p 2222 smith@125.131.198.22 "docker logs -f act-backend 2>&1"
    ;;
  2)
    echo "거래 로그 모니터링 (Ctrl+C로 종료)..."
    ssh -p 2222 smith@125.131.198.22 "docker exec act-backend tail -f /app/logs/act-application.log | grep -E '거래|주문|Trade|Order|체결|매수|매도|현재가|목표가'"
    ;;
  3)
    echo "에러 로그 모니터링 (Ctrl+C로 종료)..."
    ssh -p 2222 smith@125.131.198.22 "docker exec act-backend tail -f /app/logs/act-error.log"
    ;;
  4)
    echo "모든 로그 모니터링 (Ctrl+C로 종료)..."
    ssh -p 2222 smith@125.131.198.22 "docker exec act-backend tail -f /app/logs/act-application.log"
    ;;
  *)
    echo "잘못된 선택입니다."
    exit 1
    ;;
esac