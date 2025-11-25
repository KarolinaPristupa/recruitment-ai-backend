package com.example.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "external_vacancies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalVacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id", nullable = false)
    @JsonIgnore
    private Vacancy vacancy;

    private String externalId;
    private String externalUrl;
    private String externalStatus;
    private String billingType;
    private String vacancyType;
    private Boolean archived;

    private LocalDateTime publishedAt;
}

