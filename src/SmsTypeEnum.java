public enum SmsTypeEnum {
    SYSTEM_GENERATED("SystemGenerated"),
    REGISTRATION("Registration"),
    PROMO_AVAIL("PromoAvail");

    private final String type;

    SmsTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static SmsTypeEnum toEnum(String type) {
        for (SmsTypeEnum smsType : SmsTypeEnum.values()) {
            if (type.equals(smsType.getType())) {
                return smsType;
            }
        }
        return null;
    }
}
