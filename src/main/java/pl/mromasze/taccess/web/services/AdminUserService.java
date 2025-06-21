package pl.mromasze.taccess.web.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mromasze.taccess.web.entity.AdminUser;
import pl.mromasze.taccess.web.repository.AdminUserRepository;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    
    public AdminUser findByUsername(String username) {
        return adminUserRepository.findByUsername(username).orElse(null);
    }
    
    public void createAdminIfNotExists(String username, String password) {
        if (!adminUserRepository.existsByUsername(username)) {
            AdminUser admin = new AdminUser();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setEnabled(true);
            adminUserRepository.save(admin);
        }
    }
}