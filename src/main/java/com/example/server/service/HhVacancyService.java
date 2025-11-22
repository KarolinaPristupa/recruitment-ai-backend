package com.example.server.service;

import com.example.server.client.HhClient;
import com.example.server.dto.request.HhVacancyCreateRequest;
import com.example.server.model.ExternalVacancy;
import com.example.server.model.Vacancy;
import com.example.server.repository.ExternalVacancyRepository;
import com.example.server.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HhVacancyService {

    private final VacancyRepository vacancyRepo;
    private final ExternalVacancyRepository externalRepo;
    private final HhClient hhClient;
    private final HhAuthService authService;

    private HhVacancyCreateRequest toHhRequest(Vacancy v) {
        HhVacancyCreateRequest req = new HhVacancyCreateRequest();
        req.setName(v.getTitle());
        req.setDescription(v.getDescription() + "\n\n" + v.getRequirements());
        req.setArea(1);

        HhVacancyCreateRequest.Salary sal = new HhVacancyCreateRequest.Salary();
        sal.setFrom(v.getSalaryMin().intValue());
        sal.setTo(v.getSalaryMax().intValue());
        req.setSalary(sal);

        return req;
    }

    public ExternalVacancy publishToHh(Long vacancyId, String code) {
        log.info(">>> Публикация вакансии в HH: vacancyId={}, code={}", vacancyId, code);

        Vacancy vacancy = vacancyRepo.findById(vacancyId)
                .orElseThrow(() -> {
                    log.error("Вакансия не найдена: id={}", vacancyId);
                    return new RuntimeException("Vacancy not found");
                });

        log.info("Вакансия найдена: title={}", vacancy.getTitle());

        var token = authService.exchangeCodeForToken(code);
        log.info("Получен токен от HH: access={}", token.getAccess_token());

        var request = toHhRequest(vacancy);
        log.info("Сформирован запрос в HH: {}", request);

        var response = hhClient.createVacancy(request, token.getAccess_token());
        log.info("Ответ HH: id={}, status={}, url={}",
                response.getId(), response.getStatus(), response.getUrl());

        ExternalVacancy ext = ExternalVacancy.builder()
                .vacancy(vacancy)
                .externalId(response.getId())
                .externalUrl(response.getUrl())
                .externalStatus(response.getStatus())
                .build();

        log.info("Сохранение ExternalVacancy: {}", ext);

        var saved = externalRepo.save(ext);
        log.info(">>> Публикация завершена, сохранено: {}", saved);

        return saved;
    }

}
