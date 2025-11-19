package com.example.server.controller;

import com.example.server.dto.request.VacancyRequestDTO;
import com.example.server.dto.response.VacancyResponseDTO;
import com.example.server.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

    @PostMapping
    public ResponseEntity<VacancyResponseDTO> create(@RequestBody VacancyRequestDTO dto) {
        return ResponseEntity.ok(vacancyService.createVacancy(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacancyResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.getVacancy(id));
    }

    @GetMapping
    public ResponseEntity<List<VacancyResponseDTO>> getAll() {
        return ResponseEntity.ok(vacancyService.getAllVacancies());
    }

    @GetMapping("/my")
    public ResponseEntity<List<VacancyResponseDTO>> getMy() {
        return ResponseEntity.ok(vacancyService.getMyVacancies());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacancyResponseDTO> update(
            @PathVariable Long id,
            @RequestBody VacancyRequestDTO dto
    ) {
        return ResponseEntity.ok(vacancyService.updateVacancy(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.ok().build();
    }
}

