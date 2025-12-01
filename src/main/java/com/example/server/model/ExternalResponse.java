package com.example.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "external_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "external_vacancy_id", nullable = false)
    private ExternalVacancy externalVacancy;

    private String resumeId;
    private String fileUrl;
    private String applicantName;
    @Column(columnDefinition = "TEXT")
    private String messageText;
    private LocalDateTime dateApplied;
    private LocalDateTime dateReceived;
    private String status;
}
