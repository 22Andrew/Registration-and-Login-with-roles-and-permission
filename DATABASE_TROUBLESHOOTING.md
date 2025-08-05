# Database Troubleshooting Guide

## Issue: Users not saving to MySQL database

This guide covers the fixes implemented to ensure users are properly saved to the MySQL database.

## ✅ **Fixes Applied**

### 1. **Fixed User Entity Mapping**
- **Issue**: Join table name mismatch
- **Fix**: Changed `users_roles` to `user_roles` in `@JoinTable` annotation
- **Added**: Cascade type for better relationship handling

```java
@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
@JoinTable(
    name = "user_roles",  // Fixed from "users_roles"
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
)
```

### 2. **Enhanced UserService Logic**
- **Issue**: Role resolution conflicts in saveUser method
- **Fix**: Added proper role handling for both ID and name-based roles
- **Added**: Comprehensive debugging logs
- **Added**: `@Transactional` annotation for proper transaction management

### 3. **Fixed Password Encoding**
- **Issue**: Double encoding of passwords
- **Fix**: Removed duplicate password encoding in WebController
- **Result**: Password is now encoded only once in the service layer

### 4. **Database Initialization**
- **Added**: `DataInitializer` component to create default roles and users
- **Creates**: ADMIN and USER roles automatically
- **Creates**: Default admin user (admin@admin.com / admin)
- **Creates**: Test user (user@test.com / password)

### 5. **Enhanced Logging**
- **Added**: SQL query logging for debugging
- **Added**: Transaction logging
- **Added**: Custom debug statements in service methods

## 🔧 **How to Test the Fix**

### Step 1: Start MySQL Database
```bash
# Using Docker (recommended)
./start-mysql.sh

# Or check if MySQL is running locally
mysql -h localhost -P 3306 -u root -p'.hassan.92' -e "SHOW DATABASES;"
```

### Step 2: Start the Application
```bash
# Use the provided script
./start-app.sh

# Or manually
mvn spring-boot:run
```

### Step 3: Check Database Initialization
Look for these log messages in the console:
```
=== INITIALIZING DATABASE ===
Created ADMIN role
Created USER role
Created default admin user: admin@admin.com / admin (ID: 1)
Created test user: user@test.com / password (ID: 2)
=== DATABASE INITIALIZATION COMPLETE ===
```

### Step 4: Test User Creation
1. Go to: http://localhost:8080/admin/users
2. Click "+ Add New User"
3. Fill in the form:
   - First Name: John
   - Last Name: Doe
   - Age: 30
   - Email: john.doe@example.com
   - Password: password123
   - Role: User (or Admin)
4. Click "Add User"

### Step 5: Verify in Database
```sql
-- Connect to MySQL
mysql -h localhost -P 3306 -u root -p'.hassan.92'

-- Use the database
USE securityProject;

-- Check tables were created
SHOW TABLES;

-- Check users
SELECT * FROM users;

-- Check roles
SELECT * FROM roles;

-- Check user-role relationships
SELECT u.email, r.name 
FROM users u 
JOIN user_roles ur ON u.id = ur.user_id 
JOIN roles r ON r.id = ur.role_id;
```

## 🚨 **Troubleshooting Common Issues**

### Issue 1: "Table doesn't exist"
**Cause**: Database not properly initialized
**Solution**: 
```bash
# Check Hibernate logs for table creation
# Look for DDL statements in console output
# Verify spring.jpa.hibernate.ddl-auto=update in application.properties
```

### Issue 2: "User saved but not visible in database"
**Cause**: Transaction not committed
**Solution**: Added `@Transactional` annotation to service methods

### Issue 3: "Role not found error"
**Cause**: Roles not initialized
**Solution**: `DataInitializer` creates default roles on startup

### Issue 4: "Duplicate entry error"
**Cause**: Email uniqueness constraint
**Solution**: Use different email addresses for each user

### Issue 5: "Connection refused"
**Cause**: MySQL not running
**Solution**: 
```bash
# Using Docker
./start-mysql.sh

# Check MySQL status
mysql -h localhost -P 3306 -u root -p'.hassan.92' -e "SELECT 1;"
```

## 📊 **Debug Information**

### Console Output During User Creation
```
=== WEB CONTROLLER ADD USER ===
User data: John Doe (john.doe@example.com)
Role IDs received: [2]
Roles resolved: [[USER]]
=== SAVING USER ===
User ID: null
User Email: john.doe@example.com
User Roles: [[USER]]
Final user before save: john.doe@example.com with roles: [[USER]]
Hibernate: insert into users (age,email,first_name,last_name,password,username) values (?,?,?,?,?,?)
Hibernate: insert into user_roles (user_id,role_id) values (?,?)
User saved with ID: 3
User saved successfully!
```

### Database Queries to Verify
```sql
-- Count total users
SELECT COUNT(*) as total_users FROM users;

-- List all users with roles
SELECT 
    u.id,
    u.first_name,
    u.last_name,
    u.email,
    GROUP_CONCAT(r.name) as roles
FROM users u 
LEFT JOIN user_roles ur ON u.id = ur.user_id 
LEFT JOIN roles r ON r.id = ur.role_id 
GROUP BY u.id;

-- Check table structure
DESCRIBE users;
DESCRIBE roles;
DESCRIBE user_roles;
```

## ✅ **Expected Results**

After applying all fixes:
1. ✅ Users save successfully to MySQL database
2. ✅ Roles are properly assigned
3. ✅ User-role relationships are created
4. ✅ Users appear in the user list immediately
5. ✅ User details page shows correct information
6. ✅ Database queries return the saved users

## 📝 **Configuration Summary**

### Database Configuration (application.properties)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/securityProject?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=.hassan.92
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
```

### Key Components
- **WebController**: Handles user form submission
- **UserServiceImpl**: Business logic with transaction management
- **DataInitializer**: Creates default data on startup
- **User Entity**: Proper JPA mapping with cascade settings
- **Role Entity**: Authority implementation for Spring Security

The application now properly saves users to the MySQL database with full debugging and error handling!