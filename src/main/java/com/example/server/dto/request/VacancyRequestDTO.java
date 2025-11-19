package com.example.server.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VacancyRequestDTO {
    private String title;
    private String description;
    private String requirements;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String status;
    private String externalIds;
}

