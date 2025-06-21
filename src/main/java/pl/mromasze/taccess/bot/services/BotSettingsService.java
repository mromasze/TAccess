package pl.mromasze.taccess.bot.services;

import org.springframework.stereotype.Service;
import pl.mromasze.taccess.bot.entity.BotSettings;
import pl.mromasze.taccess.bot.enums.SettingType;
import pl.mromasze.taccess.bot.repository.BotSettingsRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BotSettingsService {
    private final BotSettingsRepository botSettingsRepository;
    
    public String getTerms() {
        return getValue(SettingType.TERMS);
    }
    
    public void setTerms(String terms, String modifiedBy) {
        setValue(SettingType.TERMS, terms, modifiedBy);
    }
    
    public String getValue(SettingType settingType) {
        return botSettingsRepository.findById(settingType.getKey())
                .map(BotSettings::getSettingValue)
                .orElse(null);
    }
    
    public void setValue(SettingType settingType, String value, String modifiedBy) {
        BotSettings settings = botSettingsRepository.findById(settingType.getKey())
                .orElse(new BotSettings(settingType, null, modifiedBy));
        settings.setSettingValue(value);
        settings.setModifiedBy(modifiedBy);
        botSettingsRepository.save(settings);
    }
    
    public List<BotSettings> getAllSettings() {
        return botSettingsRepository.findAll();
    }
    
    public Optional<BotSettings> getSetting(SettingType settingType) {
        return botSettingsRepository.findById(settingType.getKey());
    }
    
    public boolean isSettingEnabled(SettingType settingType) {
        return "true".equalsIgnoreCase(getValue(settingType));
    }
    
    public void deleteSetting(SettingType settingType) {
        botSettingsRepository.deleteById(settingType.getKey());
    }
}