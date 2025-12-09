package pl.mromasze.taccess.web.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mromasze.taccess.web.entity.AdminUser;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final AdminUserService adminUserService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AdminUserService adminUserService, PasswordEncoder passwordEncoder) {
        this.adminUserService = adminUserService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validateCredentials(String username, String password) {
        try {
            logger.debug("Attempting to validate credentials for user: {}", username);
            AdminUser user = adminUserService.findByUsername(username);
            
            if (user == null) {
                logger.warn("User not found: {}", username);
                return false;
            }
            
            if (!user.isEnabled()) {
                logger.warn("User account is disabled: {}", username);
                return false;
            }
            
            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
            if (!passwordMatches) {
                logger.warn("Invalid password for user: {}", username);
            } else {
                logger.info("User successfully authenticated: {}", username);
            }
            
            return passwordMatches;
        } catch (Exception e) {
            logger.error("Error validating credentials for user: {}", username, e);
            return false;
        }
    }
}
