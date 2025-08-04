package habsida.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiDocController {

    @GetMapping("/docs")
    public ResponseEntity<Map<String, Object>> getApiDocumentation() {
        Map<String, Object> apiDocs = new HashMap<>();
        
        apiDocs.put("title", "Spring Boot Security Demo REST API");
        apiDocs.put("version", "1.0.0");
        apiDocs.put("description", "REST API for user and role management with Spring Security");
        
        Map<String, Object> endpoints = new HashMap<>();
        
        // Authentication endpoints
        Map<String, String> authEndpoints = new HashMap<>();
        authEndpoints.put("POST /login", "Authenticate user (form data: username, password)");
        authEndpoints.put("GET /api/auth/user", "Get current authenticated user");
        authEndpoints.put("GET /api/auth/check", "Check authentication status");
        authEndpoints.put("POST /api/auth/logout", "Logout current user");
        endpoints.put("Authentication", authEndpoints);
        
        // Admin endpoints
        Map<String, String> adminEndpoints = new HashMap<>();
        adminEndpoints.put("GET /api/admin", "Get admin dashboard data (users, roles, current user)");
        adminEndpoints.put("POST /api/admin/users", "Create new user");
        adminEndpoints.put("PUT /api/admin/users", "Update existing user");
        adminEndpoints.put("DELETE /api/admin/users/{id}", "Delete user by ID");
        endpoints.put("Admin", adminEndpoints);
        
        // User endpoints
        Map<String, String> userEndpoints = new HashMap<>();
        userEndpoints.put("GET /api/user", "Get current user data");
        userEndpoints.put("POST /api/user", "Create user");
        userEndpoints.put("PUT /api/user", "Update current user");
        endpoints.put("User", userEndpoints);
        
        // Role endpoints
        Map<String, String> roleEndpoints = new HashMap<>();
        roleEndpoints.put("GET /api/admin/roles", "Get all roles");
        roleEndpoints.put("GET /api/admin/roles/{id}", "Get role by ID");
        roleEndpoints.put("POST /api/admin/roles", "Create new role");
        roleEndpoints.put("PUT /api/admin/roles/{id}", "Update role");
        roleEndpoints.put("DELETE /api/admin/roles/{id}", "Delete role");
        endpoints.put("Roles", roleEndpoints);
        
        apiDocs.put("endpoints", endpoints);
        
        Map<String, String> notes = new HashMap<>();
        notes.put("Authentication", "Use form-based login at /login endpoint");
        notes.put("Authorization", "Admin endpoints require ADMIN role, User endpoints require USER or ADMIN role");
        notes.put("Content-Type", "application/json for all API requests");
        notes.put("CORS", "Enabled for all origins");
        apiDocs.put("notes", notes);
        
        return ResponseEntity.ok(apiDocs);
    }

    @GetMapping
    public ResponseEntity<String> getApiInfo() {
        return ResponseEntity.ok("Spring Boot Security Demo REST API - Visit /api/docs for documentation");
    }
}