package pl.mromasze.taccess.web.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.mromasze.taccess.web.services.AdminUserService;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final AdminUserService adminUserService;
    
    @Value("${admin.username}")
    private String defaultUsername;
    
    @Value("${admin.password}")
    private String defaultPassword;
    
    @PostConstruct
    public void init() {
        adminUserService.createAdminIfNotExists(defaultUsername, defaultPassword);
    }
}