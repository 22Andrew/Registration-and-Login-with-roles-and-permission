#!/bin/bash

# Start MySQL Database for Spring Security Demo
# This script uses Docker Compose to start a MySQL container

echo "🚀 Starting MySQL database for Spring Security Demo..."

# Check if Docker is running
if ! docker info >/dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check if Docker Compose is available
if ! command -v docker-compose >/dev/null 2>&1; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose."
    echo "   You can use 'docker compose' (newer version) instead."
    exit 1
fi

# Start MySQL container
echo "📦 Starting MySQL container..."
docker-compose up -d mysql

# Wait for MySQL to be ready
echo "⏳ Waiting for MySQL to be ready..."
sleep 10

# Check if MySQL is running
if docker-compose ps mysql | grep -q "Up"; then
    echo "✅ MySQL is now running!"
    echo ""
    echo "📋 Database Information:"
    echo "   Host: localhost"
    echo "   Port: 3306"
    echo "   Database: securityProject"
    echo "   Username: root"
    echo "   Password: .hassan.92"
    echo ""
    echo "🔧 To connect to MySQL:"
    echo "   mysql -h localhost -P 3306 -u root -p'.hassan.92' securityProject"
    echo ""
    echo "🐳 Docker Commands:"
    echo "   Stop MySQL: docker-compose down"
    echo "   View logs: docker-compose logs mysql"
    echo "   Restart: docker-compose restart mysql"
    echo ""
    echo "🚀 You can now start your Spring Boot application:"
    echo "   mvn spring-boot:run"
else
    echo "❌ Failed to start MySQL. Check Docker logs:"
    docker-compose logs mysql
    exit 1
fi