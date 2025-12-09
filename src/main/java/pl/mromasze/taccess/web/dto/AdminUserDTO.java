package pl.mromasze.taccess.web.dto;

import pl.mromasze.taccess.web.entity.AdminUser;

public class AdminUserDTO {
    private Long id;
    private String username;
    private Boolean enabled;

    public static AdminUserDTO fromEntity(AdminUser adminUser) {
        AdminUserDTO dto = new AdminUserDTO();
        dto.setId(adminUser.getId());
        dto.setUsername(adminUser.getUsername());
        dto.setEnabled(adminUser.isEnabled());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
