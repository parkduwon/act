#!/bin/bash

echo "====================================="
echo "   ACT Backend Docker 배포"
echo "====================================="

# Configuration
REMOTE_USER="smith"
REMOTE_HOST="125.131.198.22"
REMOTE_PORT="2222"
REMOTE_DIR="/home/smith/act-backend-docker"

# Step 1: Build JAR locally
echo "▶ Gradle 빌드 중..."
cd /Users/smith/IdeaProjects/act/backend
./gradlew clean build -x test

# Step 2: Create Dockerfile
cat > /tmp/Dockerfile << 'EOF'
FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY ldk-0.0.1-SNAPSHOT.jar app.jar
COPY application-prod.yml application-prod.yml
EXPOSE 8090
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-Xms512m", "-Xmx1024m", "-Dspring.config.location=application-prod.yml", "-jar", "app.jar"]
EOF

# Step 3: Transfer files to server
echo "▶ 서버로 파일 전송 중..."
ssh -p ${REMOTE_PORT} ${REMOTE_USER}@${REMOTE_HOST} "mkdir -p ${REMOTE_DIR}"
scp -P ${REMOTE_PORT} build/libs/ldk-0.0.1-SNAPSHOT.jar ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/
scp -P ${REMOTE_PORT} src/main/resources/application-prod.yml ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/
scp -P ${REMOTE_PORT} /tmp/Dockerfile ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/

# Step 4: Build and run Docker container on server
echo "▶ Docker 컨테이너 실행 중..."
ssh -p ${REMOTE_PORT} ${REMOTE_USER}@${REMOTE_HOST} << 'EOF'
cd /home/smith/act-backend-docker

# Stop and remove existing container
docker stop act-backend 2>/dev/null || true
docker rm act-backend 2>/dev/null || true

# Build new image
docker build -t act-backend:latest .

# Run container
docker run -d \
  --name act-backend \
  -p 8090:8090 \
  --restart unless-stopped \
  act-backend:latest

# Check status
sleep 5
docker ps | grep act-backend
echo "Container logs:"
docker logs --tail 20 act-backend
EOF

rm /tmp/Dockerfile

echo "====================================="
echo "   배포 완료!"
echo "====================================="
echo "Backend URL: http://125.131.198.22:8090"
echo "로그 확인: ssh -p 2222 smith@125.131.198.22 'docker logs -f act-backend'"