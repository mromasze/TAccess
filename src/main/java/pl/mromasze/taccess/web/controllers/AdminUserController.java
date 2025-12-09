package pl.mromasze.taccess.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.mromasze.taccess.web.dto.AdminUserDTO;
import pl.mromasze.taccess.web.dto.ApiResponse;
import pl.mromasze.taccess.web.entity.AdminUser;
import pl.mromasze.taccess.web.repository.AdminUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
public class AdminUserController {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminUserDTO>>> getAllAdminUsers() {
        try {
            List<AdminUserDTO> users = adminUserRepository.findAll()
                    .stream()
                    .map(AdminUserDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch admin users: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminUserDTO>> getAdminUser(@PathVariable Long id) {
        try {
            AdminUser user = adminUserRepository.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Admin user not found"));
            }
            return ResponseEntity.ok(ApiResponse.success(AdminUserDTO.fromEntity(user)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch admin user: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdminUserDTO>> createAdminUser(@RequestBody CreateAdminUserRequest request) {
        try {
            if (adminUserRepository.existsByUsername(request.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Username already exists"));
            }

            AdminUser adminUser = new AdminUser();
            adminUser.setUsername(request.getUsername());
            adminUser.setPassword(passwordEncoder.encode(request.getPassword()));
            adminUser.setEmail(request.getEmail());
            adminUser.setEnabled(true);

            AdminUser savedUser = adminUserRepository.save(adminUser);
            return ResponseEntity.ok(ApiResponse.success("Admin user created successfully",
                    AdminUserDTO.fromEntity(savedUser)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error creating admin user: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminUserDTO>> updateAdminUser(
            @PathVariable Long id,
            @RequestBody UpdateAdminUserRequest request) {
        try {
            AdminUser user = adminUserRepository.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Admin user not found"));
            }

            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }
            if (request.getEnabled() != null) {
                user.setEnabled(request.getEnabled());
            }
            if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            AdminUser updatedUser = adminUserRepository.save(user);
            return ResponseEntity.ok(ApiResponse.success("Admin user updated successfully",
                    AdminUserDTO.fromEntity(updatedUser)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating admin user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAdminUser(@PathVariable Long id) {
        try {
            if (!adminUserRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Admin user not found"));
            }
            adminUserRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Admin user deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting admin user: " + e.getMessage()));
        }
    }

    // Inner classes for request DTOs
    public static class CreateAdminUserRequest {
        private String username;
        private String password;
        private String email;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }
    }

    public static class UpdateAdminUserRequest {
        private String email;
        private Boolean enabled;
        private String password;

        public String getEmail() {
            return email;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public String getPassword() {
            return password;
        }
    }
}