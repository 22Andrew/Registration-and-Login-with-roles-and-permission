package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.RoleService;
import habsida.spring.boot_security.demo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class WebController {

    private final UserServiceImpl userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebController(UserServiceImpl userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public String getUsersPage(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        List<User> users = userService.findAllWithRoles();
        List<Role> allRoles = roleService.findAll();
        User newUser = new User();
        
        model.addAttribute("users", users);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("newUser", newUser);
        
        return "users";
    }

    @GetMapping("/users/{id}")
    public String viewUser(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        User user = userService.findUserById(id);
        if (user == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/admin/users";
        }
        
        String email = loggedInUser.getUsername();
        User currentUser = userService.findByEmail(email).orElse(null);
        
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        
        return "user-details";
    }

    @PostMapping("/users")
    public String addUser(@ModelAttribute("newUser") User user, 
                         BindingResult result, 
                         @RequestParam("roles") List<Long> roleIds,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        
        if (result.hasErrors()) {
            List<User> users = userService.findAllWithRoles();
            List<Role> allRoles = roleService.findAll();
            model.addAttribute("users", users);
            model.addAttribute("allRoles", allRoles);
            model.addAttribute("errors", result.getAllErrors());
            return "users";
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUsername(user.getEmail());

            Set<Role> roles = roleIds.stream()
                    .map(id -> roleService.findById(id)
                            .orElseThrow(() -> new RuntimeException("Invalid role ID: " + id)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);

            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("addSuccess", "User added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add user: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam Long id,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam Integer age,
                           @RequestParam String email,
                           @RequestParam(required = false) String password,
                           @RequestParam("roleIds") List<Long> roleIds,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findUserById(id);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "User not found");
                return "redirect:/admin/users";
            }

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setAge(age);
            user.setEmail(email);
            user.setUsername(email);

            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }

            Set<Role> updatedRoles = roleIds.stream()
                    .map(roleId -> roleService.findById(roleId)
                            .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId)))
                    .collect(Collectors.toSet());
            user.setRoles(updatedRoles);

            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("editSuccess", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("deleteSuccess", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
}