package com.example.server.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class VacancyResponseDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String requirements;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String status;
    private LocalDateTime publishedAt;
    private String externalIds;
    private LocalDateTime createdAt;
}

