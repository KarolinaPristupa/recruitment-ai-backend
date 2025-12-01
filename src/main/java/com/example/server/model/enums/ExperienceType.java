package com.example.server.model.enums;

public enum ExperienceType {
    NO_EXPERIENCE("Нет опыта", "noExperience"),
    BETWEEN_1_AND_3("1-3 года", "between1And3"),
    BETWEEN_3_AND_6("3-6 лет", "between3And6"),
    MORE_6("Более 6 лет", "moreThan6");

    private final String displayName;
    private final String hhValue;

    ExperienceType(String displayName, String hhValue) {
        this.displayName = displayName;
        this.hhValue = hhValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHhValue() {
        return hhValue;
    }

    public static ExperienceType fromDto(String dtoValue) {
        if (dtoValue == null) return null;

        return switch (dtoValue) {
            case "NO_EXPERIENCE" -> NO_EXPERIENCE;
            case "1_3_YEARS" -> BETWEEN_1_AND_3;
            case "3_6_YEARS" -> BETWEEN_3_AND_6;
            case "6_PLUS_YEARS" -> MORE_6;
            default -> null;
        };
    }
}
