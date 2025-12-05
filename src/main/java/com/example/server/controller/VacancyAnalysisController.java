package com.example.server.controller;

import com.example.server.dto.response.VacancyAnalyticsDTO;
import com.example.server.model.ExternalResponse;
import com.example.server.model.ExternalVacancy;
import com.example.server.model.ResponseAnalysis;
import com.example.server.model.User;
import com.example.server.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacancyAnalysisController {

    private final ExternalVacancyService vacancyService;
    private final ExternalResponseService responseService;
    private final ResumeAnalysisService analysisService;
    private final UserService userService;
    private final VacancyAnalyticsService analyticsService;
    @PostMapping("/{vacancyId}/analyze-responses")
    public ResponseEntity<?> analyzeAllResponses(@PathVariable Long vacancyId) {
        User currentUser = userService.getCurrentUser();
        ExternalVacancy vacancy = vacancyService.getById(vacancyId);
        List<ExternalResponse> responses = responseService.getByExternalVacancy(vacancy);

        if (responses.isEmpty()) {
            return ResponseEntity.badRequest().body("На данную вакансию пока нет откликов");
        }

        analysisService.analyzeAll(vacancy, responses, currentUser);

        return ResponseEntity.ok("Анализ запущен для  " + responses.size() + " откликов");
    }

    @GetMapping("/{vacancyId}/analysis/top")
    public ResponseEntity<List<ResponseAnalysis>> getTopResults(
            @PathVariable Long vacancyId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        ExternalVacancy vacancy = vacancyService.getById(vacancyId);
        List<ResponseAnalysis> top = analysisService.getTopResults(vacancy, limit);
        return ResponseEntity.ok(top);
    }

    @GetMapping("/{vacancyId}/analytics/all")
    public ResponseEntity<VacancyAnalyticsDTO> getAllAnalytics(
            @PathVariable Long vacancyId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        VacancyAnalyticsDTO dto = new VacancyAnalyticsDTO(
                analyticsService.getResponseDelay(vacancyId),
                analyticsService.getTopSkills(vacancyId),
                analyticsService.getSkillGap(vacancyId),
                analyticsService.getStats(vacancyId),
                analyticsService.getTopResumes(vacancyId, PageRequest.of(0, limit))
        );

        return ResponseEntity.ok(dto);
    }

}
