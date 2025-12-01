package com.example.server.model.enums;

public enum WorkFormat {
    OFFICE("Офис"),
    REMOTE("Удалённая работа"),
    HYBRID("Гибрид");

    private final String displayName;

    WorkFormat(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
