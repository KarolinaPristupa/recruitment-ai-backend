package com.example.server.model.enums;

public enum EmploymentType {
    FULL_TIME("Полная занятость"),
    PART_TIME("Частичная занятость"),
    PROJECT("Проектная работа"),
    VOLUNTEER("Волонтёрство"),
    TRAINING("Стажировка");

    private final String displayName;

    EmploymentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
