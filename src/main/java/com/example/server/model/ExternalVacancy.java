package com.example.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(length = 255)
    private String externalUrl;

    @Column(length = 50)
    private String externalStatus;

    @Column(length = 50)
    private String externalId;
}
