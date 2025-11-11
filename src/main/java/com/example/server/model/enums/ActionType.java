package com.example.server.model.enums;

public enum ActionType {
    LOGIN,              // вход в систему
    LOGOUT,             // выход из системы
    CREATE_USER,        // создание нового пользователя (HR или кандидат)
    UPDATE_USER,        // редактирование пользователя
    DELETE_USER,        // удаление пользователя
    CREATE_VACANCY,     // создание вакансии
    UPDATE_VACANCY,     // редактирование вакансии
    DELETE_VACANCY,     // удаление вакансии
    VIEW_VACANCY,       // просмотр вакансии
    OTHER               // любое действие, которое не входит в список
}

