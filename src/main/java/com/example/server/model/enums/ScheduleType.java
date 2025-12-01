package com.example.server.model.enums;

public enum ScheduleType {
        FULL_DAY("Полный день", "fullDay"),
        REMOTE("Удалённо", "remote"),
        FLEXIBLE("Гибкий график", "flexible"),
        SHIFT("Сменный график", "shift"),
        FLY_IN_FLY_OUT("Вахтовый метод", "flyInFlyOut");



    private final String displayName;
    private final String hhValue;

    ScheduleType(String displayName, String hhValue) {
        this.displayName = displayName;
        this.hhValue = hhValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHhValue() {
        return hhValue;
    }

    public static ScheduleType fromDto(String dtoValue) {
        if (dtoValue == null) return null;

        return switch (dtoValue) {
            case "FULL_TIME", "FULL_DAY", "5/2", "2/2" -> FULL_DAY;
            case "PART_TIME" -> FLEXIBLE;
            case "REMOTE" -> REMOTE;
            case "SHIFT" -> SHIFT;
            case "FLY_IN_FLY_OUT" -> FLY_IN_FLY_OUT;
            default -> null;
        };
    }

}
