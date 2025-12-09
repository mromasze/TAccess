package pl.mromasze.taccess.bot.entity;

import jakarta.persistence.*;
import pl.mromasze.taccess.bot.enums.SettingType;

import java.time.LocalDateTime;

@Entity
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

    public BotSettings() {
    }

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

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public SettingType getSettingType() {
        return settingType;
    }

    public void setSettingType(SettingType settingType) {
        this.settingType = settingType;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
