package pl.mromasze.taccess.web.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.mromasze.taccess.web.services.AdminUserService;

@Component
public class AdminInitializer {
    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);
    
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "haslo123";

    private final AdminUserService adminUserService;

    public AdminInitializer(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing admin user: {}", DEFAULT_USERNAME);
        adminUserService.createAdminIfNotExists(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        logger.info("Admin user initialization completed");
    }
}
