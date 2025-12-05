package com.example.server.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HrProfileResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private Long enterpriseId;
    private String enterpriseName;
    private String enterpriseAddress;
    private String enterpriseContactEmail;
    private String enterpriseContactPhone;
}
