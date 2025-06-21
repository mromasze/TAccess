package pl.mromasze.taccess.bot.handlers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pl.mromasze.taccess.bot.TelegramBot;
import pl.mromasze.taccess.bot.entity.User;
import pl.mromasze.taccess.bot.handlers.CommandHandler;
import pl.mromasze.taccess.bot.repository.UserRepository;
import pl.mromasze.taccess.bot.services.LanguageService;

@Component
public class StartCommandHandler implements CommandHandler {
    private final LanguageService languageService;
    private final UserRepository userRepository;

    @Autowired
    public StartCommandHandler(LanguageService languageService, UserRepository userRepository) {
        this.languageService = languageService;
        this.userRepository = userRepository;
    }

    @Override
    public void handleUpdate(Update update, TelegramBot telegramBot) throws TelegramApiException {
        Long chatId;
        Integer messageId;

        // Obsługa komendy /start
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            messageId = update.getMessage().getMessageId();
            String messageText = update.getMessage().getText();

            if (messageText.equals("/start")) {
                // Sprawdzenie, czy użytkownik istnieje w bazie
                if (!userRepository.existsByTelegramId(chatId)) {
                    User user = new User();
                    user.setTelegramId(chatId);
                    user.setLanguageCode(null); // Brak języka na początku
                    userRepository.save(user);
                    telegramBot.sendLanguageSelectionMenu(chatId, null);
                } else if (!languageService.hasLanguageSelected(chatId)) {
                    // Użytkownik istnieje, ale nie wybrał języka
                    telegramBot.sendLanguageSelectionMenu(chatId, null);
                } else {
                    // Użytkownik istnieje i ma wybrany język
                    telegramBot.sendMainMenu(chatId, null);
                }
            }
            return;
        }

        // Obsługa kliknięć w przyciski inline
        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            messageId = update.getCallbackQuery().getMessage().getMessageId();
            String callbackData = update.getCallbackQuery().getData();

            switch (callbackData) {
                case "language_pl":
                    languageService.setUserLanguage(chatId, "pl");
                    telegramBot.sendMainMenu(chatId, messageId);
                    break;
                case "language_en":
                    languageService.setUserLanguage(chatId, "en");
                    telegramBot.sendMainMenu(chatId, messageId);
                    break;
                case "action_change_language":
                    telegramBot.sendLanguageSelectionMenu(chatId, messageId);
                    break;
                case "action_buy":
                    telegramBot.sendProductList(chatId, messageId);
                    break;
                case "action_terms":
                    telegramBot.sendTerms(chatId, messageId);
                    break;
                case "action_main_menu":
                    telegramBot.sendMainMenu(chatId, messageId);
                    break;
                default:
                    if (callbackData.startsWith("buy_product_")) {
                        // Póki co tylko odświeżamy listę produktów
                        telegramBot.sendProductList(chatId, messageId);
                    }
                    break;
            }
        }
    }
}