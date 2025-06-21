package pl.mromasze.taccess.bot.dto;

import lombok.Data;
import pl.mromasze.taccess.bot.enums.SettingType;
import java.time.LocalDateTime;

@Data
public class BotSettingDTO {
    private SettingType settingType;
    private String settingValue;
    private LocalDateTime lastModified;
    private String modifiedBy;
    private String description;
    private String valueType;
}