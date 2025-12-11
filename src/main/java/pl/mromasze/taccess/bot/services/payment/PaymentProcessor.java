package pl.mromasze.taccess.bot.services.payment;

import pl.mromasze.taccess.bot.dto.PaymentResponse;
import pl.mromasze.taccess.bot.entity.Order;

public interface PaymentProcessor {
    /**
     * Creates a payment for the given order.
     * @param order The order to pay for.
     * @return PaymentResponse containing text and optional QR code.
     */
    PaymentResponse createPayment(Order order);

    /**
     * Validates if this processor is enabled and configured.
     * @return true if enabled.
     */
    boolean isEnabled();
    
    /**
     * Returns the identifier of the payment method (e.g., "STRIPE", "CRYPTO").
     */
    String getMethodName();
}
