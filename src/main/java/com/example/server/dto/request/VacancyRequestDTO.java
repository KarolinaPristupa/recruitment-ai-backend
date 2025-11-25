package com.example.server.dto.request;

import com.example.server.model.enums.EmploymentType;
import com.example.server.model.enums.VacancyStatus;
import com.example.server.model.enums.WorkFormat;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VacancyRequestDTO {
    private String title;
    private String description;
    private String requirements;
    private String skills;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency;
    private EmploymentType employmentType;
    private WorkFormat workFormat;
    private String experience;
    private String schedule;
    private String category;
    private VacancyStatus status;
}
