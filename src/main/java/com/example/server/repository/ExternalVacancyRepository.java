package com.example.server.repository;

import com.example.server.model.Enterprise;
import com.example.server.model.ExternalVacancy;
import com.example.server.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExternalVacancyRepository extends JpaRepository<ExternalVacancy, Long> {
    Optional<ExternalVacancy> findByVacancy(Vacancy vacancy);
    Optional<ExternalVacancy> findByVacancyId(Long id);

}
