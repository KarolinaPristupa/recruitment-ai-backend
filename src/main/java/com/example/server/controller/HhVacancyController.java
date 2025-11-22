package com.example.server.controller;

import com.example.server.model.ExternalVacancy;
import com.example.server.service.HhVacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/hh/vacancies")
@RequiredArgsConstructor
public class HhVacancyController {

    private final HhVacancyService hhVacancyService;

    @PostMapping("/{id}/publish")
    public ExternalVacancy publish(@PathVariable Long id, @RequestParam String code) {
        log.info("POST /api/hh/vacancies/{}/publish?code={}", id, code);
        var result = hhVacancyService.publishToHh(id, code);
        log.info("Ответ клиенту: {}", result);
        return result;
    }
}


