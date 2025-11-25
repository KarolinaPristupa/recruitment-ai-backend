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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HhVacancyService {

    private final VacancyRepository vacancyRepository;
    private final ExternalVacancyRepository externalRepository;
    private final HhClient hhClient;
    private final HhAuthService authService;
    private final UserService userService;
    private final LogService logService;
    private final HhAreaService areaService;

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

 //   public ExternalVacancy publish(Long vacancyId) {
//        var hr = userService.getCurrentUser();
//        var token = authService.getTokenForHr(hr);
//
//        var vacancy = vacancyRepository.findById(vacancyId)
//                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
//
//        String city = areaService.extractCity(vacancy.getAddress());
//        String country = areaService.extractCountry(vacancy.getAddress());
//
//        Integer areaId = areaService.getAreaId(city);
//
//        if (areaId == null) {
//            areaId = hhClient.findAreaId(country, city);
//        }
//
//        if (areaId == null) {
//            throw new RuntimeException("Город не найден в HH: " + city);
//        }
//
//        HhVacancyCreateRequest req = buildRequest(vacancy, areaId);
//
//        log.info("Публикую вакансию {} в HH", vacancyId);
//
//        var response = hhClient.publish(req, token.getAccessToken());
//
//        logService.log(hr, ActionType.HH_VACANCY_PUBLISHED,
//                "Вакансия " + vacancyId + " опубликована в HH как " + response.getId());
//
//        ExternalVacancy ext = ExternalVacancy.builder()
//                .vacancy(vacancy)
//                .externalId(response.getId())
//                .externalUrl(response.getUrl())
//                .externalStatus(response.getStatus())
//                .billingType(response.getBillingType())
//                .vacancyType(response.getType())
//                .archived(response.isArchived())
//                .publishedAt(response.getPublishedAt())
//                .build();

//        return externalRepository.save(ext);
 //   }

 //   private HhVacancyCreateRequest buildRequest(Vacancy v) {
//        var hr = userService.getCurrentUser();
//        var enterprise = hr.getEnterprise();
//
//        String city = enterprise.getCity();
//        String country = enterprise.getCountry();
//
//        Integer areaId = areaService.getAreaId(city);
//        if (areaId == null) {
//            areaId = hhClient.findAreaId(country, city);
//        }
//
//        if (areaId == null) {
//            throw new RuntimeException("Город не найден в HH: " + city);
//        }
//
//        var req = new HhVacancyCreateRequest();
//        req.setName(v.getTitle());
//        req.setDescription(v.getDescription() + "\n\n" + v.getRequirements());
//        req.setArea(areaId);
//
//        if (v.getSalaryMin() != null || v.getSalaryMax() != null) {
//            var sal = new HhVacancyCreateRequest.Salary();
//            sal.setFrom(v.getSalaryMin() != null ? v.getSalaryMin().intValue() : null);
//            sal.setTo(v.getSalaryMax() != null ? v.getSalaryMax().intValue() : null);
//            req.setSalary(sal);
//        }
//
//        req.setExperience(v.getExperience());
//        req.setEmployment(v.getEmploymentType());
//        req.setSchedule(v.getSchedule());
//        req.setKeySkills(v.getSkills() != null ? List.of(v.getSkills().split("-")) : List.of());
//
//        var contacts = new HhVacancyCreateRequest.Contacts();
//        contacts.setEmail(enterprise.getContactEmail());
//        contacts.setPhone(enterprise.getContactPhone());
//        contacts.setName(hr.getFullName());
//        req.setContacts(contacts);

//        return req;
//  }
}

