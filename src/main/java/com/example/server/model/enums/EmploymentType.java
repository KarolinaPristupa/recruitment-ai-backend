package com.example.server.model.enums;

public enum EmploymentType {
    FULL_TIME("Полная занятость", "full"),
    PART_TIME("Частичная занятость", "part"),
    PROJECT("Проектная работа", "project"),
    VOLUNTEER("Волонтёрство", "volunteer"),
    TRAINING("Стажировка", "probation");

    private final String displayName;
    private final String hhValue;

    EmploymentType(String displayName, String hhValue) {
        this.displayName = displayName;
        this.hhValue = hhValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHhValue() {
        return hhValue;
    }
}
