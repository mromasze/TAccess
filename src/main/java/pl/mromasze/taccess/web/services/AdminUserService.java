package pl.mromasze.taccess.web.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mromasze.taccess.web.entity.AdminUser;
import pl.mromasze.taccess.web.repository.AdminUserRepository;

@Service
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AdminUser findByUsername(String username) {
        return adminUserRepository.findByUsername(username).orElse(null);
    }

    public void createAdminIfNotExists(String username, String password) {
        AdminUser admin = adminUserRepository.findByUsername(username).orElse(null);
        
        if (admin == null) {
            // Create new admin user
            admin = new AdminUser();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setEnabled(true);
            adminUserRepository.save(admin);
            System.out.println("Admin user created: " + username);
        } else {
            // Check if password needs to be updated by verifying if it matches
            if (!passwordEncoder.matches(password, admin.getPassword())) {
                admin.setPassword(passwordEncoder.encode(password));
                adminUserRepository.save(admin);
                System.out.println("Admin user password updated: " + username);
            }
        }
    }
}
