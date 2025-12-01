package com.example.server.service;

import com.example.server.model.ExternalVacancy;
import com.example.server.model.Vacancy;
import com.example.server.repository.ExternalVacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExternalVacancyService {

    private final ExternalVacancyRepository repository;

    public ExternalVacancy getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExternalVacancy not found with id: " + id));
    }

    public ExternalVacancy getByVacancy(Vacancy vacancy) {
        return repository.findByVacancy(vacancy)
                .orElseThrow(() -> new RuntimeException("ExternalVacancy not found for vacancy id: " + vacancy.getId()));
    }

    public ExternalVacancy createOrUpdate(ExternalVacancy externalVacancy) {
        if (externalVacancy.getPublishedAt() == null) {
            externalVacancy.setPublishedAt(LocalDateTime.now());
        }
        return repository.save(externalVacancy);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<ExternalVacancy> getByVacancyId(Long vacancyId) {
        return repository.findByVacancyId(vacancyId);
    }

}
