package pl.mromasze.taccess.web.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pl.mromasze.taccess.bot.enums.SettingType;
import pl.mromasze.taccess.bot.services.BotSettingsService;

import java.util.Arrays;

@Component
public class BotSettingsInitializer {

    private final BotSettingsService botSettingsService;

    public BotSettingsInitializer(BotSettingsService botSettingsService) {
        this.botSettingsService = botSettingsService;
    }

    @PostConstruct
    public void init() {
        Arrays.stream(SettingType.values()).forEach(settingType -> {
            if (botSettingsService.getSetting(settingType).isEmpty()) {
                String defaultValue = getDefaultValue(settingType);
                botSettingsService.setValue(settingType, defaultValue, "system");
            }
        });
    }

    private String getDefaultValue(SettingType settingType) {
        switch (settingType) {
            case TERMS:
                return "Default Terms and Conditions...";
            case WELCOME_MESSAGE:
                return "Welcome to our bot!";
            case MAINTENANCE_MODE:
                return "false";
            case MIN_PAYMENT:
                return "10.00";
            default:
                return "";
        }
    }
}
