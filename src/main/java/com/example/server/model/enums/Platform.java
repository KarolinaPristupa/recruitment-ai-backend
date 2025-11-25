package com.example.server.model.enums;

public enum Platform {
    HH_RU("Россия", 113),
    RABOTA_BY("Беларусь", 16),
    HH_KZ("Казахстан", 40),
    HH_UZ("Узбекистан", 2282),
    HH_AZ("Азербайджан", 34),
    HEADHUNTER_GE("Грузия", 157),
    HEADHUNTER_KG("Киргизия", 48);

    private final String country;
    private final int defaultAreaId;

    Platform(String country, int defaultAreaId) {
        this.country = country;
        this.defaultAreaId = defaultAreaId;
    }

    public int getDefaultAreaId() {
        return defaultAreaId;
    }

    public static Platform fromCountry(String country) {
        for (Platform p : values()) {
            if (p.country.equalsIgnoreCase(country.trim())) return p;
        }
        return null;
    }
}
