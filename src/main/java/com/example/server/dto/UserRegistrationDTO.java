package com.example.server.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Long enterpriseId;
}
