package com.example.server.controller;

import com.example.server.service.HhAuthService;
import com.example.server.service.HhVacancyService;
import com.example.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/hh/vacancies")
@RequiredArgsConstructor
public class HhVacancyController {

    private final HhVacancyService hhVacancyService;
    private final UserService userService;
    private final HhAuthService hhAuthService;

    @PostMapping("/{id}/publish")
    public ResponseEntity<?> publish(@PathVariable Long id) {
        var hr = userService.getCurrentUser();

        if (!hhAuthService.hasTokenForHr(hr.getId())) {
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", "HR не авторизован в HH");
            body.put("loginUrl", hhAuthService.getLoginUrl(hr.getId()));
            return ResponseEntity.status(401).body(body);
        }

        try {
            var result = hhVacancyService.publish(id);

            Map<String, Object> body = new HashMap<>();
            body.put("success", true);
            body.put("message", "Вакансия успешно опубликована в HH");
            body.put("externalId", result != null ? result.getExternalId() : null);
            body.put("url", result != null ? result.getExternalUrl() : null);

            return ResponseEntity.ok(body);

        } catch (Exception ex) {
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", ex.getMessage());
            return ResponseEntity.status(500).body(body);
        }
    }


}


