package com.example.server.exception;

import org.springframework.http.HttpStatus;

public class EnterpriseEmailExistsException extends BusinessException {
    public EnterpriseEmailExistsException(String email) {
        super("ENTERPRISE_EMAIL_EXISTS", HttpStatus.CONFLICT,
                "Компания с email " + email + " уже зарегистрирована");
    }
}
