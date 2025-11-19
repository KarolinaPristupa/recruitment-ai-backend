package com.example.server.dto.request;

import lombok.Data;

@Data
public class EnterpriseWithAdminRegistrationDTO {
    private String name;
    private String address;
    private String contactEmail;
    private String contactPhone;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
}
