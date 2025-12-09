package pl.mromasze.taccess.bot.enums;

public enum SettingType {
    TERMS("Terms and Conditions", "text"),
    WELCOME_MESSAGE("Welcome Message", "text"),
    MAINTENANCE_MODE("Maintenance Mode", "boolean"),
    MIN_PAYMENT("Minimum Payment Amount", "number");

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
