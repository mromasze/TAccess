package pl.mromasze.taccess.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SettingType {
    TERMS("Terms and Conditions", "text"),
    WELCOME_MESSAGE("Welcome Message", "text"),
    MAINTENANCE_MODE("Maintenance Mode", "boolean"),
    MIN_PAYMENT("Minimum Payment Amount", "number");
    
    private final String description;
    private final String valueType;
    
    public String getKey() {
        return this.name().toLowerCase();
    }
}