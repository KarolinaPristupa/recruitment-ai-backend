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

    public EnterpriseWithAdminRegistrationDTO(UserRegistrationDTO dto) {
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.email = dto.getEmail();
        this.phone = dto.getPhone();
        this.password = dto.getPassword();
    }
}
