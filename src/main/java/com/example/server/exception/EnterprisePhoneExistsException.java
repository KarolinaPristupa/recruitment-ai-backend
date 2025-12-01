package com.example.server.exception;

import org.springframework.http.HttpStatus;

public class EnterprisePhoneExistsException extends BusinessException {
    public EnterprisePhoneExistsException(String phone) {
        super("ENTERPRISE_PHONE_EXISTS", HttpStatus.CONFLICT,
                "Компания с телефоном " + phone + " уже зарегистрирована");
    }
}
