package com.example.server.repository;

import com.example.server.model.ResponseAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResumeAnalysisRepository extends JpaRepository<ResponseAnalysis, Long> {

    @Query("""
            SELECT ra FROM ResponseAnalysis ra
            WHERE ra.externalResponse.externalVacancy.id = :vacancyId
            """)
    List<ResponseAnalysis> findByVacancyId(Long vacancyId);

    @Query("""
        SELECT ra FROM ResponseAnalysis ra
        WHERE ra.externalResponse.externalVacancy.id = :vacancyId
        ORDER BY ra.score DESC
        """)
    List<ResponseAnalysis> findTopByVacancyId(Long vacancyId, org.springframework.data.domain.Pageable pageable);

}
