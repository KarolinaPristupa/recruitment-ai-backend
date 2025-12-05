package com.example.server.model.enums;

public enum ActionType {
    LOGIN,              // вход в систему
    CREATE_ENTERPRISE,        // создание нового предприятия
    UPDATE_ENTERPRISE,        // редактирование предприятия
    DELETE_ENTERPRISE,     // удаление предприятия
    CREATE_ADMIN,        // создание нового администратора
    UPDATE_ADMIN,        // редактирование администратора
    DELETE_ADMIN,       // удаление администратора
    CREATE_HR,        // создание нового HR-а
    UPDATE_HR,        // редактирование HR-а
    DELETE_HR,       // удаление HR-а
    READ_VACANCY,   // выведены вакансии
    CREATE_VACANCY,     // создание вакансии
    UPDATE_VACANCY,     // редактирование вакансии
    DELETE_VACANCY,     // удаление вакансии
    VIEW_VACANCY,       // просмотр вакансии
    HH_LOGIN,            // вход на HH (через OAuth)
    HH_TOKEN_SAVED,      // сохранение токена HH
    HH_TOKEN_REFRESHED,  // обновление токена HH
    HH_LOGOUT,          // выход/отзыв токена HH
    HH_VACANCY_PUBLISHED, // вакансия была опубликована
    FETCH_RESPONSES, // Загружены отклики
    ANALYZE_RESPONSE, // проанализированы отклики
    OTHER               // любое действие, которое не входит в список
}

