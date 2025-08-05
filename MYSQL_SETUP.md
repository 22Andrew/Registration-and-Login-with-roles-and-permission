# MySQL Database Setup Guide

This guide will help you set up MySQL database for the Spring Boot Security Demo application.

## Prerequisites

- MySQL Server 8.0 or higher
- MySQL Client tools

## Setup Instructions

### 1. Install MySQL Server

#### On Ubuntu/Debian:
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

#### On CentOS/RHEL:
```bash
sudo yum install mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld
```

#### On macOS (using Homebrew):
```bash
brew install mysql
brew services start mysql
```

#### On Windows:
Download and install MySQL from the official website: https://dev.mysql.com/downloads/mysql/

### 2. Secure MySQL Installation

```bash
sudo mysql_secure_installation
```

Follow the prompts to:
- Set root password to: `.hassan.92`
- Remove anonymous users
- Disallow root login remotely
- Remove test database
- Reload privilege tables

### 3. Create Database and User

```sql
-- Connect to MySQL as root
mysql -u root -p

-- Create the database
CREATE DATABASE securityProject;

-- Create a user for the application (optional, you can use root)
CREATE USER 'springuser'@'localhost' IDENTIFIED BY '.hassan.92';
GRANT ALL PRIVILEGES ON securityProject.* TO 'springuser'@'localhost';
FLUSH PRIVILEGES;

-- Exit MySQL
EXIT;
```

### 4. Verify Database Connection

```bash
mysql -u root -p.hassan.92 -e "SHOW DATABASES;"
```

You should see `securityProject` in the list.

### 5. Application Configuration

The application is already configured with the following settings in `src/main/resources/application.properties`:

```properties
# Database Configuration - MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/securityProject?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=.hassan.92
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### 6. Docker Alternative (Recommended for Development)

If you prefer to use Docker for MySQL:

```bash
# Pull and run MySQL container
docker run --name mysql-db \
  -e MYSQL_ROOT_PASSWORD='.hassan.92' \
  -e MYSQL_DATABASE='securityProject' \
  -p 3306:3306 \
  -d mysql:8.0

# Wait for MySQL to start (about 30 seconds)
sleep 30

# Verify connection
docker exec -it mysql-db mysql -u root -p'.hassan.92' -e "SHOW DATABASES;"
```

### 7. Run the Application

Once MySQL is set up and running:

```bash
cd /path/to/your/project
mvn spring-boot:run
```

The application will automatically create the necessary tables using Hibernate DDL.

### 8. Access the Application

- **User List Page**: http://localhost:8080/admin/users
- **User Details**: Click the eye button (👁️) on any user in the list
- **Default Login**: Use the credentials set up during application initialization

## Troubleshooting

### Connection Issues

1. **Connection refused**: Make sure MySQL is running
   ```bash
   sudo systemctl status mysql
   ```

2. **Access denied**: Check username and password
   ```bash
   mysql -u root -p'.hassan.92'
   ```

3. **Database not found**: Make sure `securityProject` database exists
   ```sql
   SHOW DATABASES;
   ```

### Application Issues

1. **Check application logs** for detailed error messages
2. **Verify database connection** using MySQL client
3. **Ensure correct port** (3306) is not blocked by firewall

## Database Schema

The application will automatically create these tables:
- `users` - User information
- `roles` - User roles (ADMIN, USER)
- `user_roles` - Many-to-many relationship between users and roles

## Sample Data

The application includes a data initialization component that will create:
- Default admin user
- Sample roles (ADMIN, USER)
- Sample test users

This happens automatically when the application starts with an empty database.