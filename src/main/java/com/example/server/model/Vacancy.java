package com.example.server.model;

import com.example.server.model.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacancies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String skills;

    private BigDecimal salaryMin;
    private BigDecimal salaryMax;

    @Column(length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    private WorkFormat workFormat;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience")
    private ExperienceType experience;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule")
    private ScheduleType schedule;

    @Column(length = 50)
    private String category;

    @Enumerated(EnumType.STRING)
    private VacancyStatus status;

    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String externalId;
}

