package pl.mromasze.taccess.bot.services.payment.impl;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.mromasze.taccess.bot.dto.PaymentResponse;
import pl.mromasze.taccess.bot.entity.Order;
import pl.mromasze.taccess.bot.enums.SettingType;
import pl.mromasze.taccess.bot.services.BotSettingsService;
import pl.mromasze.taccess.bot.services.payment.PaymentProcessor;

import java.math.BigDecimal;

@Service
public class StripePaymentProcessor implements PaymentProcessor {

    private static final Logger log = LoggerFactory.getLogger(StripePaymentProcessor.class);
    private final BotSettingsService settingsService;

    public StripePaymentProcessor(BotSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Override
    public PaymentResponse createPayment(Order order) {
        String secretKey = settingsService.getValue(SettingType.STRIPE_SECRET_KEY);
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("Stripe Secret Key is not configured.");
        }
        Stripe.apiKey = secretKey;

        String currency = settingsService.getValue(SettingType.STORE_CURRENCY);
        if (currency == null) currency = "usd";

        // Build product data
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://t.me/YOUR_BOT_NAME?start=success_" + order.getId()) // Placeholder
                .setCancelUrl("https://t.me/YOUR_BOT_NAME?start=cancel_" + order.getId())   // Placeholder
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount(order.getProduct().getPrice().multiply(new BigDecimal("100")).longValue())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(order.getProduct().getName())
                                                                .build())
                                                .build())
                                .build())
                .setClientReferenceId(order.getId().toString())
                .build();

        try {
            Session session = Session.create(params);
            return PaymentResponse.textOnly("Click the link to pay with Card:\n" + session.getUrl());
        } catch (Exception e) {
            log.error("Failed to create Stripe session", e);
            throw new RuntimeException("Failed to initiate Stripe payment.");
        }
    }

    @Override
    public boolean isEnabled() {
        return "true".equalsIgnoreCase(settingsService.getValue(SettingType.STRIPE_ENABLED));
    }

    @Override
    public String getMethodName() {
        return "STRIPE";
    }
}
