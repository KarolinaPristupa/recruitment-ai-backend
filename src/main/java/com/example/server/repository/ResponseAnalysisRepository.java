package com.example.server.repository;

import com.example.server.model.ExternalResponse;
import com.example.server.model.ResponseAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResponseAnalysisRepository extends JpaRepository<ResponseAnalysis, Long> {

    boolean existsByExternalResponse(ExternalResponse response);

    @Query("""
        SELECT ra FROM ResponseAnalysis ra 
        WHERE ra.externalResponse.externalVacancy.id = :vacancyId
        ORDER BY ra.matchPercent DESC
    """)
    List<ResponseAnalysis> findTopByVacancy(Long vacancyId, int limit);
}
