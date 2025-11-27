package com.example.server.dto.response;

import com.example.server.model.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class VacancyResponseDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String requirements;
    private String skills;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal salaryMin;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal salaryMax;
    private String currency;

    private String city;
    private String country;
    private Integer hhAreaId;

    private EmploymentType employmentType;
    private WorkFormat workFormat;
    private ExperienceType experience;
    private ScheduleType schedule;

    private VacancyStatus status;
    private LocalDateTime publishedAt;
    private String externalId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<String> keySkills;
}

