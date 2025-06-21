package pl.mromasze.taccess.bot.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mromasze.taccess.bot.enums.SettingType;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class BotSettings {
    @Id
    private String settingKey;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String settingValue;
    
    @Enumerated(EnumType.STRING)
    private SettingType settingType;
    
    private LocalDateTime lastModified;
    
    private String modifiedBy;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastModified = LocalDateTime.now();
    }
    
    public BotSettings(SettingType settingType, String settingValue, String modifiedBy) {
        this.settingKey = settingType.getKey();
        this.settingType = settingType;
        this.settingValue = settingValue;
        this.modifiedBy = modifiedBy;
    }
}