package com.example.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal salaryMin;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal salaryMax;
    private String status;
    private LocalDateTime publishedAt;
    private String externalIds;
    private LocalDateTime createdAt;
}

