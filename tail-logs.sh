#!/bin/bash

# 간단한 실시간 로그 출력 스크립트

echo "====================================="
echo "   ACT 실시간 로그 모니터링"
echo "====================================="
echo ""

# 거래 관련 로그만 필터링해서 실시간 출력
ssh -p 2222 smith@125.131.198.22 << 'EOF'
echo "거래 로그 실시간 출력 (Ctrl+C로 종료)..."
echo "----------------------------------------"
docker exec act-backend tail -f /app/logs/act-application.log | \
grep --line-buffered -E "거래|주문|Trade|Order|체결|매수|매도|현재가|목표가|ERROR|Exception" | \
while IFS= read -r line; do
    # 시간 추출
    time=$(echo "$line" | cut -d' ' -f1-2)
    # 중요 정보 하이라이트
    if echo "$line" | grep -q "ERROR\|Exception"; then
        echo -e "\033[0;31m[ERROR] $line\033[0m"  # 빨간색
    elif echo "$line" | grep -q "주문 성공\|체결"; then
        echo -e "\033[0;32m[ORDER] $line\033[0m"  # 초록색
    elif echo "$line" | grep -q "매수\|매도"; then
        echo -e "\033[0;33m[TRADE] $line\033[0m"  # 노란색
    else
        echo "$line"
    fi
done
EOF