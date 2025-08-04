package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.RoleService;
import habsida.spring.boot_security.demo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserServiceImpl userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<?> getAdminData(@AuthenticationPrincipal UserDetails loggedInUser) {
        try {
            List<User> users = userService.findAllWithRoles();
            String email = loggedInUser.getUsername();
            User currentUser = userService.findByEmail(email).orElse(null);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }
            List<Role> allRoles = roleService.findAll();
            return ResponseEntity.ok(new AdminData(users, currentUser, allRoles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching admin data: " + e.getMessage());
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        try {
            User user = new User();
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setAge(userDTO.getAge());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setUsername(userDTO.getEmail());

            Set<Role> roles = userDTO.getRoleIds().stream()
                    .map(id -> roleService.findById(id)
                            .orElseThrow(() -> new RuntimeException("Invalid role ID: " + id)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);

            userService.saveUser(user);
            return ResponseEntity.ok("User added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of(new FieldError("user", "general", "Failed to add user: " + e.getMessage())));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete user: " + e.getMessage());
        }
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, HttpSession session) {
        try {
            User user = userService.findUserById(userDTO.getId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of(new FieldError("user", "id", "User not found")));
            }

            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setAge(userDTO.getAge());
            user.setEmail(userDTO.getEmail());
            user.setUsername(userDTO.getEmail());

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }

            Set<Role> updatedRoles = userDTO.getRoleIds().stream()
                    .map(roleId -> roleService.findById(roleId)
                            .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId)))
                    .collect(Collectors.toSet());
            user.setRoles(updatedRoles);

            userService.saveUser(user);

            if (SecurityContextHolder.getContext().getAuthentication().getName().equals(userDTO.getEmail())) {
                SecurityContextHolder.clearContext();
                session.invalidate();
            }

            return ResponseEntity.ok("User updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of(new FieldError("user", "general", "Failed to update user: " + e.getMessage())));
        }
    }

    // DTO class to handle JSON input
    public static class UserDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private Integer age;
        private String email;
        private String password;
        private List<Long> roleIds;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public List<Long> getRoleIds() { return roleIds; }
        public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }
    }

    // Response wrapper for initial admin data
    public static class AdminData {
        private List<User> users;
        private User currentUser;
        private List<Role> allRoles;

        public AdminData(List<User> users, User currentUser, List<Role> allRoles) {
            this.users = users;
            this.currentUser = currentUser;
            this.allRoles = allRoles;
        }

        public List<User> getUsers() { return users; }
        public User getCurrentUser() { return currentUser; }
        public List<Role> getAllRoles() { return allRoles; }
    }
}