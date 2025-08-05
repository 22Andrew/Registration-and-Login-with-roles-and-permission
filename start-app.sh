#!/bin/bash

echo "🚀 Starting Spring Boot Security Demo Application..."

# Check if MySQL is running
echo "🔍 Checking MySQL connection..."
if mysql -h localhost -P 3306 -u root -p'.hassan.92' -e "SELECT 1;" 2>/dev/null; then
    echo "✅ MySQL is running and accessible"
else
    echo "❌ MySQL is not running or not accessible"
    echo "📝 Please start MySQL first:"
    echo "   - Using Docker: ./start-mysql.sh"
    echo "   - Or install locally: See MYSQL_SETUP.md"
    exit 1
fi

# Check if database exists
echo "🔍 Checking if database 'securityProject' exists..."
if mysql -h localhost -P 3306 -u root -p'.hassan.92' -e "USE securityProject;" 2>/dev/null; then
    echo "✅ Database 'securityProject' exists"
else
    echo "⚠️  Database 'securityProject' doesn't exist. Creating it..."
    mysql -h localhost -P 3306 -u root -p'.hassan.92' -e "CREATE DATABASE IF NOT EXISTS securityProject;"
    echo "✅ Database created"
fi

# Clean and compile
echo "🔧 Cleaning and compiling..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful"
else
    echo "❌ Compilation failed"
    exit 1
fi

# Start the application
echo "🚀 Starting Spring Boot application..."
echo "📊 Console output will show database initialization and user creation"
echo "🌐 Application will be available at: http://localhost:8080/admin/users"
echo "👤 Default login: admin@admin.com / admin"
echo ""
echo "=== APPLICATION LOG ==="

mvn spring-boot:run