-- MySQL Initialization Script for Spring Security Demo
-- This script is automatically executed when the database container starts

USE securityProject;

-- Ensure the database exists
CREATE DATABASE IF NOT EXISTS securityProject;

-- Grant all privileges to the application users
GRANT ALL PRIVILEGES ON securityProject.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON securityProject.* TO 'springuser'@'%';
FLUSH PRIVILEGES;

-- The application will automatically create tables using Hibernate DDL
-- But you can also pre-create them here if needed:

/*
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    age INT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Insert default roles
INSERT IGNORE INTO roles (name) VALUES ('ADMIN');
INSERT IGNORE INTO roles (name) VALUES ('USER');
*/

-- Print success message
SELECT 'MySQL database securityProject initialized successfully!' AS status;