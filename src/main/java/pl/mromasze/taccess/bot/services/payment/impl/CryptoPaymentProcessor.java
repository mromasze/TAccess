package pl.mromasze.taccess.bot.services.payment.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.mromasze.taccess.bot.dto.PaymentResponse;
import pl.mromasze.taccess.bot.entity.Order;
import pl.mromasze.taccess.bot.enums.SettingType;
import pl.mromasze.taccess.bot.services.BotSettingsService;
import pl.mromasze.taccess.bot.services.payment.PaymentProcessor;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class CryptoPaymentProcessor implements PaymentProcessor {

    private static final Logger log = LoggerFactory.getLogger(CryptoPaymentProcessor.class);
    private final BotSettingsService settingsService;
    private final RestTemplate restTemplate;

    public CryptoPaymentProcessor(BotSettingsService settingsService, RestTemplate restTemplate) {
        this.settingsService = settingsService;
        this.restTemplate = restTemplate;
    }

    // Example API URL - in real scenario this would be https://api.nowpayments.io/v1/payment
    private static final String API_URL = "https://api.nowpayments.io/v1/payment";

    @Override
    public PaymentResponse createPayment(Order order) {
        String apiKey = settingsService.getValue(SettingType.CRYPTO_API_KEY);
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("Crypto API Key is not configured.");
        }
        
        String currency = settingsService.getValue(SettingType.STORE_CURRENCY);
        if (currency == null || currency.trim().isEmpty()) currency = "USD";

        String payCurrency = order.getPaymentNetwork();
        if (payCurrency == null || payCurrency.trim().isEmpty()) {
             payCurrency = settingsService.getValue(SettingType.CRYPTO_PAY_CURRENCY);
        }
        if (payCurrency == null || payCurrency.trim().isEmpty()) payCurrency = "usdttrc20";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("price_amount", order.getProduct().getPrice());
            body.put("price_currency", currency);
            body.put("pay_currency", payCurrency);
            body.put("order_id", order.getId().toString());
            body.put("order_description", order.getProduct().getName());
            
            // Add callback URL properly
            String webhookUrl = "https://YOUR_DOMAIN/api/webhook/crypto"; 
            // body.put("ipn_callback_url", webhookUrl);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> respBody = response.getBody();
                String payAddress = (String) respBody.get("pay_address");
                Double payAmount = Double.valueOf(respBody.get("pay_amount").toString());
                String responseCurrency = (String) respBody.get("pay_currency");
                
                // Format message
                String message = String.format(
                        "Please send exactly: %s %s\nTo address:\n`%s`\n\n(Tap address to copy)\n\nPayment valid for 20 minutes.",
                        payAmount, responseCurrency.toUpperCase(), payAddress
                );

                // Generate QR Code
                byte[] qrCode = generateQRCode(payAddress, 300, 300);

                return PaymentResponse.withPhoto(message, qrCode);

            } else {
                throw new RuntimeException("API Error: " + response.getStatusCode());
            }
            
        } catch (HttpClientErrorException e) {
            log.error("Crypto API Client Error: {}", e.getMessage());
            String errorBody = e.getResponseBodyAsString();
            if (errorBody.contains("less than minimal")) {
                return PaymentResponse.textOnly("⚠️ Payment Error: The product price is too low for the selected cryptocurrency (" + payCurrency + "). Please contact admin or try a different product.");
            }
            return PaymentResponse.textOnly("⚠️ Payment Provider Error: " + e.getStatusText());
        } catch (Exception e) {
            log.error("Failed to create Crypto payment", e);
            return PaymentResponse.textOnly("Error generating crypto invoice. Please contact admin.");
        }
    }

    private byte[] generateQRCode(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    @Override
    public boolean isEnabled() {
        return "true".equalsIgnoreCase(settingsService.getValue(SettingType.CRYPTO_ENABLED));
    }

    @Override
    public String getMethodName() {
        return "CRYPTO";
    }
}
