package pl.mromasze.taccess.bot.handlers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pl.mromasze.taccess.bot.TelegramBot;
import pl.mromasze.taccess.bot.dto.PaymentResponse;
import pl.mromasze.taccess.bot.entity.Order;
import pl.mromasze.taccess.bot.entity.User;
import pl.mromasze.taccess.bot.entity.product.Product;
import pl.mromasze.taccess.bot.handlers.CommandHandler;
import pl.mromasze.taccess.bot.repository.OrderRepository;
import pl.mromasze.taccess.bot.repository.ProductRepository;
import pl.mromasze.taccess.bot.repository.UserRepository;
import pl.mromasze.taccess.bot.services.LanguageService;
import pl.mromasze.taccess.bot.services.payment.PaymentProcessor;
import pl.mromasze.taccess.bot.services.payment.PaymentService;
import pl.mromasze.taccess.bot.enums.SettingType;
import pl.mromasze.taccess.bot.services.BotSettingsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class StartCommandHandler implements CommandHandler {
    private final LanguageService languageService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final BotSettingsService botSettingsService;

    @Autowired
    public StartCommandHandler(LanguageService languageService, UserRepository userRepository,
                               ProductRepository productRepository, OrderRepository orderRepository,
                               PaymentService paymentService, BotSettingsService botSettingsService) {
        this.languageService = languageService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.botSettingsService = botSettingsService;
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
                if (!userRepository.existsByTelegramId(chatId)) {
                    User user = new User();
                    user.setTelegramId(chatId);
                    userRepository.save(user);
                    telegramBot.sendLanguageSelectionMenu(chatId, null);
                } else if (!languageService.hasLanguageSelected(chatId)) {
                    telegramBot.sendLanguageSelectionMenu(chatId, null);
                } else {
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
                        Long productId = Long.parseLong(callbackData.replace("buy_product_", ""));
                        Product product = productRepository.findById(productId).orElse(null);
                        User user = userRepository.findByTelegramId(chatId).orElse(null);

                        if (product != null && user != null) {
                            Order order = new Order();
                            order.setProduct(product);
                            order.setUser(user);
                            order.setPurchaseDate(LocalDateTime.now());
                            order.setStatus(pl.mromasze.taccess.bot.enums.PaymentStatus.PENDING);
                            order = orderRepository.save(order);

                            List<PaymentProcessor> processors = paymentService.getEnabledProcessors();
                            if (processors.isEmpty()) {
                                telegramBot.sendPaymentInfo(chatId, messageId, PaymentResponse.textOnly("No payment methods available."));
                            } else if (processors.size() == 1) {
                                // For single processor, handle crypto specifically or general
                                PaymentProcessor processor = processors.get(0);
                                if ("CRYPTO".equalsIgnoreCase(processor.getMethodName())) {
                                    handleCryptoFlow(chatId, messageId, order.getId(), telegramBot);
                                } else {
                                    PaymentResponse info = paymentService.initiatePayment(order.getId(), processor.getMethodName());
                                    telegramBot.sendPaymentInfo(chatId, messageId, info);
                                }
                            } else {
                                telegramBot.sendPaymentMethodSelection(chatId, messageId, order.getId(), processors);
                            }
                        }
                    } else if (callbackData.startsWith("pay_method_")) {
                        // format: pay_method_{orderId}_{methodName}
                        String[] parts = callbackData.split("_");
                        if (parts.length >= 4) {
                            Long orderId = Long.parseLong(parts[2]);
                            String methodName = parts[3];
                            
                            if ("CRYPTO".equalsIgnoreCase(methodName)) {
                                handleCryptoFlow(chatId, messageId, orderId, telegramBot);
                            } else {
                                try {
                                    PaymentResponse info = paymentService.initiatePayment(orderId, methodName);
                                    telegramBot.sendPaymentInfo(chatId, messageId, info);
                                } catch (Exception e) {
                                    telegramBot.sendPaymentInfo(chatId, messageId, PaymentResponse.textOnly("Error: " + e.getMessage()));
                                }
                            }
                        }
                    } else if (callbackData.startsWith("pay_crypto_")) {
                        // format: pay_crypto_{orderId}_{network}
                        String[] parts = callbackData.split("_");
                        if (parts.length >= 4) {
                            Long orderId = Long.parseLong(parts[2]);
                            String network = parts[3];
                            
                            Order order = orderRepository.findById(orderId).orElse(null);
                            if (order != null) {
                                order.setPaymentNetwork(network);
                                orderRepository.save(order);
                                try {
                                    PaymentResponse info = paymentService.initiatePayment(orderId, "CRYPTO");
                                    telegramBot.sendPaymentInfo(chatId, messageId, info);
                                } catch (Exception e) {
                                    telegramBot.sendPaymentInfo(chatId, messageId, PaymentResponse.textOnly("Error: " + e.getMessage()));
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void handleCryptoFlow(Long chatId, Integer messageId, Long orderId, TelegramBot telegramBot) throws TelegramApiException {
        String networksStr = botSettingsService.getValue(SettingType.CRYPTO_AVAILABLE_CURRENCIES);
        List<String> networks = new ArrayList<>();
        if (networksStr != null && !networksStr.isEmpty()) {
            networks = Arrays.asList(networksStr.split(","));
        }
        
        if (networks.size() > 1) {
            telegramBot.sendCryptoNetworkSelection(chatId, messageId, orderId, networks);
        } else {
            // Auto select the only network or default
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null && !networks.isEmpty()) {
                order.setPaymentNetwork(networks.get(0));
                orderRepository.save(order);
            }
            
            try {
                PaymentResponse info = paymentService.initiatePayment(orderId, "CRYPTO");
                telegramBot.sendPaymentInfo(chatId, messageId, info);
            } catch (Exception e) {
                telegramBot.sendPaymentInfo(chatId, messageId, PaymentResponse.textOnly("Error: " + e.getMessage()));
            }
        }
    }
}