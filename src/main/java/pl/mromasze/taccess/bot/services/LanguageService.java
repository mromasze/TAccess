package pl.mromasze.taccess.bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mromasze.taccess.bot.entity.User;
import pl.mromasze.taccess.bot.repository.UserRepository;

import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class LanguageService {
    private final UserRepository userRepository;
    private static final String DEFAULT_LANGUAGE = "en";

    @Autowired
    public LanguageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Locale getUserLanguage(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .map(user -> {
                    String langCode = user.getLanguageCode();
                    return langCode != null ? new Locale(langCode) : new Locale(DEFAULT_LANGUAGE);
                })
                .orElse(new Locale(DEFAULT_LANGUAGE));
    }

    public String getMessage(Long telegramId, String key) {
        Locale locale = getUserLanguage(telegramId);
        ResourceBundle messages = ResourceBundle.getBundle("lang.messages", locale);
        return messages.getString(key);
    }

    public void setUserLanguage(Long telegramId, String languageCode) {
        User user = userRepository.findByTelegramId(telegramId).orElse(new User());
        if (user.getId() == null) {
            user.setTelegramId(telegramId);
        }
        user.setLanguageCode(languageCode);
        userRepository.save(user);
    }

    public boolean hasLanguageSelected(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .map(user -> user.getLanguageCode() != null)
                .orElse(false);
    }
}