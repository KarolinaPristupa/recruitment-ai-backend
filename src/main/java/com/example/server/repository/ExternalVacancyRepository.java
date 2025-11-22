package com.example.server.repository;

import com.example.server.model.Enterprise;
import com.example.server.model.ExternalVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExternalVacancyRepository extends JpaRepository<ExternalVacancy, Long> {

}
