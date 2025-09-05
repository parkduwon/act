#!/bin/bash

echo "====================================="
echo "   ACT Frontend Docker 배포"
echo "====================================="

# Configuration
REMOTE_USER="smith"
REMOTE_HOST="125.131.198.22"
REMOTE_PORT="2222"
REMOTE_DIR="/home/smith/act-frontend-docker"

# Step 1: Build frontend
echo "▶ Frontend 빌드 중..."
cd /Users/smith/IdeaProjects/act/front
npm run build

# Step 2: Create Dockerfile and nginx config
cat > /tmp/Dockerfile << 'EOF'
FROM nginx:alpine
COPY dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
EOF

cat > /tmp/nginx.conf << 'EOF'
server {
    listen 80;
    server_name _;
    
    root /usr/share/nginx/html;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    error_page 404 /index.html;
}
EOF

# Step 3: Transfer files
echo "▶ 서버로 파일 전송 중..."
ssh -p ${REMOTE_PORT} ${REMOTE_USER}@${REMOTE_HOST} "rm -rf ${REMOTE_DIR} && mkdir -p ${REMOTE_DIR}/dist"
scp -P ${REMOTE_PORT} -r dist/* ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/dist/
scp -P ${REMOTE_PORT} /tmp/Dockerfile ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/
scp -P ${REMOTE_PORT} /tmp/nginx.conf ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/

# Step 4: Build and run Docker container
echo "▶ Docker 컨테이너 실행 중..."
ssh -p ${REMOTE_PORT} ${REMOTE_USER}@${REMOTE_HOST} << 'EOF'
cd /home/smith/act-frontend-docker

# Stop and remove existing container
docker stop act-frontend 2>/dev/null || true
docker rm act-frontend 2>/dev/null || true

# Build new image
docker build -t act-frontend:latest .

# Run container
docker run -d \
  --name act-frontend \
  -p 3001:80 \
  --restart unless-stopped \
  act-frontend:latest

# Check status
docker ps | grep act-frontend
EOF

rm /tmp/Dockerfile /tmp/nginx.conf

echo "====================================="
echo "   배포 완료!"
echo "====================================="
echo "Frontend URL: http://125.131.198.22:3001"