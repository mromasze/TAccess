package pl.mromasze.taccess.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import pl.mromasze.taccess.bot.entity.BotSettings;
import pl.mromasze.taccess.bot.enums.SettingType;
import pl.mromasze.taccess.bot.handlers.CommandHandler;
import pl.mromasze.taccess.bot.config.TelegramBotConfig;
import pl.mromasze.taccess.bot.entity.product.Product;
import pl.mromasze.taccess.bot.repository.ProductRepository;
import pl.mromasze.taccess.bot.services.BotSettingsService;
import pl.mromasze.taccess.bot.services.LanguageService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());
    private final TelegramBotConfig botConfig;
    private final CommandHandler commandHandler;
    private final LanguageService languageService;
    private final BotSettingsService botSettingsService;
    private final ProductRepository productRepository;

    @Autowired
    public TelegramBot(TelegramBotConfig botConfig, CommandHandler commandHandler,
                       LanguageService languageService, BotSettingsService botSettingsService, BotSettingsService botSettingsService1,
                       ProductRepository productRepository) {

        this.botConfig = botConfig;
        this.commandHandler = commandHandler;
        this.languageService = languageService;
        this.botSettingsService = botSettingsService1;
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            logger.severe("Error registering bot: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            commandHandler.handleUpdate(update, this);
        } catch (TelegramApiException e) {
            logger.severe("Error processing update: " + e.getMessage());
        }
    }

    public void sendLanguageSelectionMenu(Long chatId, Integer messageIdToDelete) throws TelegramApiException {
        if (messageIdToDelete != null) {
            deleteMessage(chatId, messageIdToDelete);
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(languageService.getMessage(chatId, "welcome.message"));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton plButton = new InlineKeyboardButton();
        plButton.setText(languageService.getMessage(chatId, "language.pl"));
        plButton.setCallbackData("language_pl");
        row.add(plButton);

        InlineKeyboardButton enButton = new InlineKeyboardButton();
        enButton.setText(languageService.getMessage(chatId, "language.en"));
        enButton.setCallbackData("language_en");
        row.add(enButton);

        keyboard.add(row);
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

        execute(message);
    }

    public void sendTerms(Long chatId, Integer messageIdToDelete) throws TelegramApiException {
        if (messageIdToDelete != null) {
            deleteMessage(chatId, messageIdToDelete);
        }

        Optional<BotSettings> termsSettings = botSettingsService.getSetting(SettingType.TERMS);
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.enableHtml(true);

        if (termsSettings.isEmpty() || termsSettings.get().getSettingValue() == null
                || termsSettings.get().getSettingValue().trim().isEmpty()) {
            message.setText(languageService.getMessage(chatId, "terms.not_set"));
        } else {
            BotSettings terms = termsSettings.get();
            String formattedMessage = String.format("%s\n\n%s\n\n<i>Last updated: %s</i>",
                    languageService.getMessage(chatId, "terms.title"),
                    terms.getSettingValue(),
                    terms.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            message.setText(formattedMessage);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText(languageService.getMessage(chatId, "terms.back"));
        backButton.setCallbackData("action_main_menu");
        row.add(backButton);
        
        keyboard.add(row);
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

        execute(message);
    }


    public void sendMainMenu(Long chatId, Integer messageIdToDelete) throws TelegramApiException {
        if (messageIdToDelete != null) {
            deleteMessage(chatId, messageIdToDelete);
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(languageService.getMessage(chatId, "menu.title"));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton buyButton = new InlineKeyboardButton();
        buyButton.setText(languageService.getMessage(chatId, "menu.buy"));
        buyButton.setCallbackData("action_buy");
        row1.add(buyButton);

        InlineKeyboardButton termsButton = new InlineKeyboardButton();
        termsButton.setText(languageService.getMessage(chatId, "menu.terms"));
        termsButton.setCallbackData("action_terms");
        row1.add(termsButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton changeLanguageButton = new InlineKeyboardButton();
        changeLanguageButton.setText(languageService.getMessage(chatId, "menu.change_language"));
        changeLanguageButton.setCallbackData("action_change_language");
        row2.add(changeLanguageButton);

        keyboard.add(row1);
        keyboard.add(row2);
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

        execute(message);
    }

    public void sendProductList(Long chatId, Integer messageIdToDelete) throws TelegramApiException {
        if (messageIdToDelete != null) {
            deleteMessage(chatId, messageIdToDelete);
        }

        List<Product> products = productRepository.findAll();
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        if (products.isEmpty()) {
            message.setText(languageService.getMessage(chatId, "products.empty"));
        } else {
            message.setText(languageService.getMessage(chatId, "products.available"));

        for (Product product : products) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton productButton = new InlineKeyboardButton();
            productButton.setText(product.getName() + " - " + product.getPrice() + " BTC");
            productButton.setCallbackData("buy_product_" + product.getId());
            row.add(productButton);
            keyboard.add(row);
        }
    }

    List<InlineKeyboardButton> backRow = new ArrayList<>();
    InlineKeyboardButton backButton = new InlineKeyboardButton();
    backButton.setText(languageService.getMessage(chatId, "products.back"));
    backButton.setCallbackData("action_main_menu");
    backRow.add(backButton);
    keyboard.add(backRow);

    markup.setKeyboard(keyboard);
    message.setReplyMarkup(markup);

    execute(message);
}


    private void deleteMessage(Long chatId, Integer messageId) throws TelegramApiException {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId.toString());
        deleteMessage.setMessageId(messageId);
        execute(deleteMessage);
    }
}