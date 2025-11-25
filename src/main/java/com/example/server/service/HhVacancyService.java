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
@RequiredArgsConstructor
@Slf4j
public class HhVacancyService {

    private final VacancyRepository vacancyRepository;
    private final ExternalVacancyRepository externalVacancyRepository;
    private final HhClient client;
    private final UserService userService;
    private final HhAuthService authService;

    private HhVacancyCreateRequest toHhRequest(Vacancy v) {
        HhVacancyCreateRequest req = new HhVacancyCreateRequest();
        req.setName(v.getTitle());
        req.setDescription(v.getDescription() + "\n\n" + v.getRequirements());
        req.setArea(1);

        if (v.getSalaryMin() != null && v.getSalaryMax() != null) {
            var sal = new HhVacancyCreateRequest.Salary();
            sal.setFrom(v.getSalaryMin().intValue());
            sal.setTo(v.getSalaryMax().intValue());
            req.setSalary(sal);
        }

        return req;
    }

    public ExternalVacancy publishToHh(Long vacancyId) {
        var hr = userService.getCurrentUser();

        if (!authService.hasTokenForHr(hr.getId())) {
            throw new RuntimeException("HH token missing");
        }

        var token = authService.getTokenForHr(hr);

        var vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        var request = toHhRequest(vacancy);

        log.info("Публикую вакансию {} в HH", vacancyId);
        var response = client.publish(request, token.getAccessToken());
        log.info("HH ответил {}", response);

        ExternalVacancy ext = ExternalVacancy.builder()
                .vacancy(vacancy)
                .externalId(response.getId())
                .externalUrl(response.getUrl())
                .externalStatus("ACTIVE")
                .build();

        return externalVacancyRepository.save(ext);
    }

}

