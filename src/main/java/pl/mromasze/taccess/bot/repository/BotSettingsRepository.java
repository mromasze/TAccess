package pl.mromasze.taccess.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mromasze.taccess.bot.entity.BotSettings;
import pl.mromasze.taccess.bot.enums.SettingType;

import java.util.List;
import java.util.Optional;

@Repository
public interface BotSettingsRepository extends JpaRepository<BotSettings, String> {

    Optional<BotSettings> findBySettingType(SettingType settingType);
    List<BotSettings> findByModifiedBy(String modifiedBy);
    List<BotSettings> findAllByOrderByLastModifiedDesc();
    boolean existsBySettingType(SettingType settingType);
    void deleteBySettingType(SettingType settingType);
}