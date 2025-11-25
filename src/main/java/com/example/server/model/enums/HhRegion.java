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
    OMSK("Омск", 42),
    VORONEZH("Воронеж", 26),
    MINSK("Минск", 1577),
    GOMEL("Гомель", 1586),
    GRODNO("Гродно", 1585),
    BREST("Брест", 1583),
    MOGILEV("Могилёв", 1587),
    VITEBSK("Витебск", 1584),
    ALMATY("Алматы", 160),
    ASTANA("Астана", 159),
    SHYMKENT("Шымкент", 2442),
    KARAGANDA("Караганда", 205),
    AKTOBE("Актобе", 178),
    PAVLODAR("Павлодар", 233),
    URALSK("Уральск", 220),
    TASHKENT("Ташкент", 2761),
    SAMARKAND("Самарканд", 2814),
    NAMANGAN("Наманган", 2806),
    FERGANA("Фергана", 2810),
    BUKHARA("Бухара", 2798),
    ANDIJAN("Андижан", 2790),
    BAKU("Баку", 3262),
    SUMQAYIT("Сумгаит", 3294),
    GANJA("Гянджа", 3281),
    MINGACHEVIR("Мингечевир", 3290),
    TBILISI("Тбилиси", 4931),
    KUTAISI("Кутаиси", 4932),
    BATUMI("Батуми", 4933),
    RUSTAVI("Рустави", 4934),
    BISHKEK("Бишкек", 4401),
    OSH("Ош", 4406),
    JALAL_ABAD("Джалал-Абад", 4407),
    KARAKOL("Каракол", 4408),
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

