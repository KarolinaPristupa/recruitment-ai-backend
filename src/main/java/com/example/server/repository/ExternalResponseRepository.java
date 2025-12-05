package com.example.server.repository;

import com.example.server.model.ExternalResponse;
import com.example.server.model.ExternalVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExternalResponseRepository extends JpaRepository<ExternalResponse, Long> {
    List<ExternalResponse> findByExternalVacancy(ExternalVacancy externalVacancy);
    boolean existsByExternalVacancyAndExternalResponseId(
            ExternalVacancy vacancy,
            String externalResponseId
    );

}

