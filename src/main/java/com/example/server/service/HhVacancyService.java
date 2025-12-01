package com.example.server.service;

import com.example.server.client.HhClient;
import com.example.server.dto.request.HhVacancyCreateRequest;
import com.example.server.model.ExternalVacancy;
import com.example.server.model.Vacancy;
import com.example.server.model.enums.ActionType;
import com.example.server.model.enums.VacancyStatus;
import com.example.server.repository.ExternalVacancyRepository;
import com.example.server.repository.VacancyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HhVacancyService {

    private final HhSpecializationService specializationService;
    private final VacancyRepository vacancyRepository;
    private final ExternalVacancyRepository externalRepository;
    private final HhClient hhClient;
    private final HhAuthService authService;
    private final UserService userService;
    private final LogService logService;
    private final HhAreaService areaService;

    public ExternalVacancy publish(Long vacancyId) throws JsonProcessingException {
        var hr = userService.getCurrentUser();

        var token = authService.getTokenForHr(hr);
        if (token == null) {
            throw new RuntimeException("Нет токена HH для HR: " + hr.getId());
        }

        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        var enterprise = vacancy.getUser().getEnterprise();
        if (enterprise == null) {
            throw new RuntimeException("У пользователя нет предприятия");
        }

        String city = areaService.extractCity(enterprise.getAddress());
        String country = areaService.extractCountry(enterprise.getAddress());
        Integer areaId = areaService.getAreaId(country, city);
        if (areaId == null) {
            throw new RuntimeException("Город не найден в HH: " + city);
        }

        HhVacancyCreateRequest req = new HhVacancyCreateRequest();
        req.setName(vacancy.getTitle());
        req.setDescription(vacancy.getDescription() + "\n\n" + vacancy.getRequirements());

        // --- AREA ---
        var area = new HhVacancyCreateRequest.Area();
        area.setId(areaId.toString());
        req.setArea(area);

        // --- SALARY ---
        if (vacancy.getSalaryMin() != null || vacancy.getSalaryMax() != null) {
            HhVacancyCreateRequest.Salary salary = new HhVacancyCreateRequest.Salary();
            salary.setFrom(vacancy.getSalaryMin() != null ? vacancy.getSalaryMin().intValue() : null);
            salary.setTo(vacancy.getSalaryMax() != null ? vacancy.getSalaryMax().intValue() : null);
            req.setSalary(salary);
        }

        // --- EMPLOYMENT ---
        var employment = new HhVacancyCreateRequest.Employment();
        employment.setId(vacancy.getEmploymentType() != null ?
                vacancy.getEmploymentType().getHhValue() : null);

        // --- EXPERIENCE ---
        var experience = new HhVacancyCreateRequest.Experience();
        experience.setId(vacancy.getExperience() != null ?
                vacancy.getExperience().getHhValue() : null);

        // --- SCHEDULE ---
        var schedule = new HhVacancyCreateRequest.Schedule();
        schedule.setId(vacancy.getSchedule() != null ?
                vacancy.getSchedule().getHhValue() : null);

        req.setEmployment(employment);
        req.setExperience(experience);
        req.setSchedule(schedule);

        // --- KEY SKILLS ---
        req.setKeySkills(vacancy.getSkills() != null ?
                Arrays.stream(vacancy.getSkills().split("-"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(s -> {
                            HhVacancyCreateRequest.KeySkill ks = new HhVacancyCreateRequest.KeySkill();
                            ks.setName(s);
                            return ks;
                        })
                        .toList()
                : List.of()
        );

        HhVacancyCreateRequest.VacancyProperties vp = new HhVacancyCreateRequest.VacancyProperties();
        HhVacancyCreateRequest.Property prop = new HhVacancyCreateRequest.Property();
        prop.setPropertyType("HH_STANDARD");
        vp.setProperties(List.of(prop));
        req.setVacancyProperties(vp);

        // --- SPECIALIZATION AUTODETECT ---
        String specializationId = specializationService.getSpecializationCode(vacancy.getTitle());
        if (specializationId != null) {
            HhVacancyCreateRequest.Specialization spec = new HhVacancyCreateRequest.Specialization();
            spec.setId(specializationId);
            req.setSpecializations(List.of(spec));
        }

        // --- CONTACTS ---
        HhVacancyCreateRequest.Contacts contacts = new HhVacancyCreateRequest.Contacts();
        contacts.setName(hr.getFirstName() + " " + hr.getLastName());
        contacts.setEmail(enterprise.getContactEmail());
        contacts.setPhone(enterprise.getContactPhone());
        req.setContacts(contacts);

        // --- LOG REQUEST ---
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        log.info(mapper.writeValueAsString(req));

        // --- SEND TO HH ---
        var response = hhClient.publish(req, token.getAccessToken());

        if (response == null || response.getId() == null) {
            throw new RuntimeException("HH вернул пустой ответ");
        }

        // --- SAVE EXTERNAL ENTRY ---
        ExternalVacancy ext = ExternalVacancy.builder()
                .vacancy(vacancy)
                .externalId(response.getId())
                .externalUrl(response.getUrl())
                .publishedAt(LocalDateTime.now())
                .archived(false)
                .build();

        externalRepository.save(ext);

        // --- SYSTEM LOG ---
        logService.log(hr, ActionType.HH_VACANCY_PUBLISHED,
                "Вакансия " + vacancy.getId() + " опубликована в HH как " + response.getId());

        // --- UPDATE STATUS ---
        vacancy.setStatus(VacancyStatus.ACTIVE);
        if (vacancy.getPublishedAt() == null) {
            vacancy.setPublishedAt(LocalDateTime.now());
        }
        vacancyRepository.save(vacancy);

        return ext;
    }

}
