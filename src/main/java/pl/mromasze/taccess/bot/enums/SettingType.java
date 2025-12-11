package pl.mromasze.taccess.bot.enums;

public enum SettingType {
    TERMS("Terms and Conditions", "text"),
    WELCOME_MESSAGE("Welcome Message", "text"),
    MAINTENANCE_MODE("Maintenance Mode", "boolean"),
    MIN_PAYMENT("Minimum Payment Amount", "number"),
    
    // Payment General
    STORE_CURRENCY("Store Currency (e.g., USD, PLN)", "text"),

    // Stripe
    STRIPE_ENABLED("Enable Stripe Payments", "boolean"),
    STRIPE_PUBLIC_KEY("Stripe Public Key", "text"),
    STRIPE_SECRET_KEY("Stripe Secret Key", "text"),
    STRIPE_WEBHOOK_SECRET("Stripe Webhook Secret", "text"),

    // Crypto (e.g., NowPayments or Generic)
    CRYPTO_ENABLED("Enable Crypto Payments", "boolean"),
    CRYPTO_PROVIDER("Crypto Provider", "text"),
    CRYPTO_PAY_CURRENCY("Default Crypto Payout Currency (fallback)", "text"),
    CRYPTO_AVAILABLE_CURRENCIES("Available Crypto Currencies (comma separated, e.g. btc,eth,usdttrc20)", "text"),
    CRYPTO_API_KEY("Crypto Provider API Key", "text"),
    CRYPTO_WEBHOOK_SECRET("Crypto Webhook Secret", "text");

    private final String description;
    private final String valueType;

    SettingType(String description, String valueType) {
        this.description = description;
        this.valueType = valueType;
    }

    public String getDescription() {
        return description;
    }

    public String getValueType() {
        return valueType;
    }

    public String getKey() {
        return this.name().toLowerCase();
    }
}
