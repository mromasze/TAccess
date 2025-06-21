package pl.mromasze.taccess.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pl.mromasze.taccess.bot.TelegramBot;

public interface CommandHandler {
    void handleUpdate(Update update, TelegramBot telegramBot) throws TelegramApiException;
}