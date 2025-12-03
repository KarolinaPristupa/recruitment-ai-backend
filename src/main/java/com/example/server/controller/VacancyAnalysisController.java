package com.example.server.controller;

import com.example.server.model.ExternalResponse;
import com.example.server.model.ExternalVacancy;
import com.example.server.model.ResponseAnalysis;
import com.example.server.model.User;
import com.example.server.service.ExternalResponseService;
import com.example.server.service.ExternalVacancyService;
import com.example.server.service.ResumeAnalysisService;
import com.example.server.service.UserService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/{vacancyId}/analyze-responses")
    public ResponseEntity<?> analyzeAllResponses(@PathVariable Long vacancyId) {
        User currentUser = userService.getCurrentUser();
        ExternalVacancy vacancy = vacancyService.getById(vacancyId);
        List<ExternalResponse> responses = responseService.getByExternalVacancy(vacancy);

        if (responses.isEmpty()) {
            return ResponseEntity.badRequest().body("No responses found for this vacancy");
        }

        analysisService.analyzeAll(vacancy, responses, currentUser);

        return ResponseEntity.ok("AI analysis started for " + responses.size() + " responses");
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
}
