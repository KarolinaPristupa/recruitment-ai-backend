package com.example.server.repository;

import com.example.server.model.Vacancy;
import com.example.server.model.enums.VacancyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findByUserId(Long userId);
    List<Vacancy> findByUserIdAndStatus(Long userId, VacancyStatus status);
}
