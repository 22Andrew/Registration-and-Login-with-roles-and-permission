package habsida.spring.boot_security.demo.config;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.RoleService;
import habsida.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INITIALIZING DATABASE ===");
        
        // Create roles if they don't exist
        Role adminRole = roleService.findByName("ADMIN").orElse(null);
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ADMIN");
            roleService.save(adminRole);
            System.out.println("Created ADMIN role");
        }

        Role userRole = roleService.findByName("USER").orElse(null);
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleService.save(userRole);
            System.out.println("Created USER role");
        }

        // Create admin user if it doesn't exist
        if (userService.findByEmail("admin@admin.com").isEmpty()) {
            System.out.println("Creating admin user...");
            User adminUser = new User();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setAge(30);
            adminUser.setEmail("admin@admin.com");
            adminUser.setUsername("admin@admin.com");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminUser.setRoles(adminRoles);
            
            // Use repository directly for initialization
            adminUser = ((UserServiceImpl) userService).getUserRepository().save(adminUser);
            System.out.println("Created default admin user: admin@admin.com / admin (ID: " + adminUser.getId() + ")");
        }

        // Create a test user if it doesn't exist
        if (userService.findByEmail("user@test.com").isEmpty()) {
            System.out.println("Creating test user...");
            User testUser = new User();
            testUser.setFirstName("Test");
            testUser.setLastName("User");
            testUser.setAge(25);
            testUser.setEmail("user@test.com");
            testUser.setUsername("user@test.com");
            testUser.setPassword(passwordEncoder.encode("password"));
            
            Set<Role> testUserRoles = new HashSet<>();
            testUserRoles.add(userRole);
            testUser.setRoles(testUserRoles);
            
            // Use repository directly for initialization
            testUser = ((UserServiceImpl) userService).getUserRepository().save(testUser);
            System.out.println("Created test user: user@test.com / password (ID: " + testUser.getId() + ")");
        }

        System.out.println("=== DATABASE INITIALIZATION COMPLETE ===");
        System.out.println("Available roles: " + roleService.findAll().size());
        System.out.println("Available users: " + userService.findAllUsers().size());
    }
}