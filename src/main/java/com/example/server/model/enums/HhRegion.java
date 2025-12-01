package com.example.server.model.enums;


import java.util.HashMap;
import java.util.Map;

public enum HhRegion {
    MOSCOW("Москва", 1),
    ST_PETERSBURG("Санкт-Петербург", 2),
    NOVOSIBIRSK("Новосибирск", 4),
    EKATERINBURG("Екатеринбург", 3),
    NIZHNY_NOVGOROD("Нижний Новгород", 66),
    KAZAN("Казань", 88),
    SAMARA("Самара", 78),
    ROSTOV_ON_DON("Ростов-на-Дону", 76),
    KRASNODAR("Краснодар", 53),
    UFA("Уфа", 99),
    VOLGOGRAD("Волгоград", 24),
    PERM("Пермь", 72),
    CHELYABINSK("Челябинск", 104),
    OMSK("Омск", 68),
    VORONEZH("Воронеж", 26),
    MINSK("Минск", 1002),
    GOMEL("Гомель", 1003),
    GRODNO("Гродно", 1006),
    BREST("Брест", 1007),
    MOGILEV("Могилёв", 1004),
    VITEBSK("Витебск", 1005),
    ALMATY("Алматы", 160),
    ASTANA("Астана", 159),
    SHYMKENT("Шымкент", 205),
    KARAGANDA("Караганда", 177),
    AKTOBE("Актобе", 154),
    PAVLODAR("Павлодар", 181),
    URALSK("Уральск", 193),
    TASHKENT("Ташкент", 2759),
    SAMARKAND("Самарканд", 2778),
    NAMANGAN("Наманган", 2779),
    FERGANA("Фергана", 2782),
    BUKHARA("Бухара", 2781),
    ANDIJAN("Андижан", 2768),
    BAKU("Баку", 2492),
    SUMQAYIT("Сумгаит", 2495),
    GANJA("Гянджа", 2494),
    TBILISI("Тбилиси", 2758),
    KUTAISI("Кутаиси", 2813),
    BATUMI("Батуми", 2814),
    RUSTAVI("Рустави", 2815),
    BISHKEK("Бишкек", 2760),
    OSH("Ош", 2796),
    JALAL_ABAD("Джалал-Абад", 2795),
    KARAKOL("Каракол", 2792),
;
    private final String name;
    private final int id;

    HhRegion(String name, int id) {
        this.name = name;
        this.id = id;
    }

    private static final Map<String, Integer> CITY_MAP = new HashMap<>();

    static {
        for (HhRegion r : values()) {
            CITY_MAP.put(r.name.toLowerCase(), r.id);
        }
    }

    public static Integer getIdByCity(String city) {
        if (city == null) return null;
        city = city.toLowerCase();
        return CITY_MAP.getOrDefault(city, null);
    }
}

