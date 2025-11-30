package com.example.server.controller;

import com.example.server.model.ExternalVacancy;
import com.example.server.model.ExternalResponse;
import com.example.server.model.HhToken;
import com.example.server.model.User;
import com.example.server.service.ExternalResponseService;
import com.example.server.service.ExternalVacancyService;
import com.example.server.service.HhTokenService;
import com.example.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/external-responses")
@RequiredArgsConstructor
public class ExternalResponseController {

    private final ExternalResponseService responseService;
    private final ExternalVacancyService externalVacancyService;
    private final UserService userService;

    @PostMapping("/fetch/{vacancyId}")
    public ResponseEntity<List<ExternalResponse>> fetchResponses(@PathVariable Long vacancyId) {
        ExternalVacancy externalVacancy = externalVacancyService.getByVacancyId(vacancyId)
                .orElseThrow(() -> new RuntimeException("Вакансия ещё не опубликована на HH"));

        User currentUser = userService.getCurrentUser();
        List<ExternalResponse> responses = responseService.fetchResponses(externalVacancy, currentUser);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{vacancyId}")
    public ResponseEntity<List<ExternalResponse>> getResponses(@PathVariable Long vacancyId) {
        ExternalVacancy externalVacancy = externalVacancyService.getByVacancyId(vacancyId)
                .orElseThrow(() -> new RuntimeException("Вакансия ещё не опубликована на HH"));

        List<ExternalResponse> responses = responseService.getByExternalVacancy(externalVacancy);
        return ResponseEntity.ok(responses);
    }
}
