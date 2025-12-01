package com.example.server.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum HhSpecialization {

    DEVELOPER("Программист, разработчик", "96"),
    ANALYST("Аналитик", "10"),
    TESTER("Тестировщик", "124"),
    DESIGNER("Дизайнер, художник", "34"),
    SYSADMIN("Системный администратор", "113"),
    MARKETER("Менеджер по маркетингу, интернет-маркетолог", "68"),
    ACCOUNTANT("Бухгалтер", "18");


    private final String name;
    private final String id;

    HhSpecialization(String name, String id) {
        this.name = name;
        this.id = id;
    }

    private static final Map<String, String> NAME_TO_ID = new HashMap<>();
    static {
        for (HhSpecialization s : values()) {
            NAME_TO_ID.put(s.name.toLowerCase(), s.id);
        }
    }

    public static String getIdByName(String name) {
        if (name == null) return null;
        return NAME_TO_ID.get(name.toLowerCase());
    }
}
