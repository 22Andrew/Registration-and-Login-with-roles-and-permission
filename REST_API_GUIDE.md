# Spring Boot Security Demo - REST API Guide

## Overview
This application has been converted from a traditional MVC Spring Boot application to a REST API. All controllers now return JSON responses instead of rendering views.

## API Endpoints

### Authentication Endpoints
- **POST** `/login` - Authenticate user (form data: username, password)
- **GET** `/api/auth/user` - Get current authenticated user
- **GET** `/api/auth/check` - Check authentication status
- **POST** `/api/auth/logout` - Logout current user

### Admin Endpoints (Requires ADMIN role)
- **GET** `/api/admin` - Get admin dashboard data (users, roles, current user)
- **POST** `/api/admin/users` - Create new user
- **PUT** `/api/admin/users` - Update existing user
- **DELETE** `/api/admin/users/{id}` - Delete user by ID

### User Endpoints (Requires USER or ADMIN role)
- **GET** `/api/user` - Get current user data
- **POST** `/api/user` - Create user
- **PUT** `/api/user` - Update current user

### Role Endpoints (Requires ADMIN role)
- **GET** `/api/admin/roles` - Get all roles
- **GET** `/api/admin/roles/{id}` - Get role by ID
- **POST** `/api/admin/roles` - Create new role
- **PUT** `/api/admin/roles/{id}` - Update role
- **DELETE** `/api/admin/roles/{id}` - Delete role

### Documentation
- **GET** `/api` - API information
- **GET** `/api/docs` - Complete API documentation

## Authentication

The application uses form-based authentication. To authenticate:

1. Send a POST request to `/login` with form data:
   ```
   Content-Type: application/x-www-form-urlencoded
   
   username=your_email@example.com&password=your_password
   ```

2. On successful authentication, you'll receive a JSON response:
   ```json
   {
     "success": true,
     "message": "Authentication successful",
     "username": "user@example.com",
     "roles": ["ROLE_USER", "ROLE_ADMIN"]
   }
   ```

3. The session cookie will be set automatically for subsequent requests.

## Request/Response Format

### Create User Request (POST /api/admin/users)
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "age": 30,
  "email": "john.doe@example.com",
  "password": "password123",
  "roleIds": [1, 2]
}
```

### Update User Request (PUT /api/admin/users)
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "age": 31,
  "email": "john.doe@example.com",
  "password": "newpassword123",
  "roleIds": [1, 2]
}
```

### Admin Data Response (GET /api/admin)
```json
{
  "users": [...],
  "currentUser": {...},
  "allRoles": [...]
}
```

## Error Responses

### Authentication Required (401)
```json
{
  "error": "Authentication required"
}
```

### Access Denied (403)
```json
{
  "error": "Access denied"
}
```

### Bad Request (400)
```json
[
  {
    "field": "email",
    "defaultMessage": "Email is required"
  }
]
```

## CORS Configuration

CORS is enabled for all origins and methods. The following headers are allowed:
- All origins (`*`)
- Methods: GET, POST, PUT, DELETE, OPTIONS
- All headers
- Credentials: enabled

## Testing the API

You can test the API using tools like:
- **Postman**: Import the endpoints and test with proper authentication
- **cURL**: Command line testing
- **Browser**: For GET endpoints after authentication

### Example cURL Commands

1. **Authentication:**
   ```bash
   curl -X POST http://localhost:8080/login \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "username=admin@example.com&password=admin123" \
     -c cookies.txt
   ```

2. **Get Admin Data:**
   ```bash
   curl -X GET http://localhost:8080/api/admin \
     -H "Content-Type: application/json" \
     -b cookies.txt
   ```

3. **Create User:**
   ```bash
   curl -X POST http://localhost:8080/api/admin/users \
     -H "Content-Type: application/json" \
     -b cookies.txt \
     -d '{
       "firstName": "Test",
       "lastName": "User",
       "age": 25,
       "email": "test@example.com",
       "password": "password123",
       "roleIds": [1]
     }'
   ```

## Configuration Changes

### Security Configuration
- CSRF disabled for REST API
- CORS enabled
- JSON error responses
- Form-based authentication maintained
- Session-based authentication

### Application Properties
- Removed Thymeleaf configuration
- Added JSON serialization settings
- Added debug logging for security

### Controllers Converted
- `AdminController` → REST controller with JSON responses
- `UserController` → REST controller with JSON responses  
- `RoleController` → REST controller with JSON responses
- Added `AuthController` for authentication endpoints
- Added `ApiDocController` for documentation

## Database
The application still uses the same MySQL database configuration. Make sure your database is running and accessible with the credentials in `application.properties`.

## Running the Application
1. Start your MySQL server
2. Run the Spring Boot application: `mvn spring-boot:run`
3. The API will be available at `http://localhost:8080`
4. Visit `http://localhost:8080/api/docs` for complete API documentation