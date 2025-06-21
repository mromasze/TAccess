package pl.mromasze.taccess.web.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mromasze.taccess.web.entity.AdminUser;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AdminUserService adminUserService;
    private final PasswordEncoder passwordEncoder;

    public boolean validateCredentials(String username, String password) {
        try {
            AdminUser user = adminUserService.findByUsername(username);
            return user != null && 
                   user.isEnabled() && 
                   passwordEncoder.matches(password, user.getPassword());
        } catch (Exception e) {
            return false;
        }
    }
}