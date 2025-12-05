package com.example.server.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "response_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "external_response_id", nullable = false)
    private ExternalResponse externalResponse;

    @Column(columnDefinition = "TEXT")
    private String analysisJson;

    private Double score;
    private Double experienceYears;
    private String seniority;
    private Double matchPercent;
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String skillsJson;
}
