package com.example.server.service;

import com.example.server.dto.request.VacancyRequestDTO;
import com.example.server.dto.response.VacancyResponseDTO;
import com.example.server.model.User;
import com.example.server.model.Vacancy;
import com.example.server.model.enums.ActionType;
import com.example.server.model.enums.VacancyStatus;
import com.example.server.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final UserService userService;
    private final LogService logService;
    private final EnterpriseService enterpriseService;
    private final HhAreaService hhAreaService;

    private void checkIsHR(User user) {
        if (!"HR".equals(user.getRole().getName())) {
            throw new RuntimeException("Access denied: only HR can create or manage vacancies");
        }
    }

    public VacancyResponseDTO createVacancy(VacancyRequestDTO dto) {
        User currentUser = userService.getCurrentUser();
        checkIsHR(currentUser);

        var enterprise = enterpriseService.getByUserId(currentUser.getId());
        String city = hhAreaService.extractCity(enterprise.getAddress());
        String country = hhAreaService.extractCountry(enterprise.getAddress());
        var platform = hhAreaService.getPlatformByCountry(country);

        Vacancy vacancy = Vacancy.builder()
                .user(currentUser)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .requirements(dto.getRequirements())
                .skills(dto.getSkills())
                .salaryMin(dto.getSalaryMin())
                .salaryMax(dto.getSalaryMax())
                .currency(dto.getCurrency())
                .employmentType(dto.getEmploymentType())
                .workFormat(dto.getWorkFormat())
                .experience(dto.getExperience())
                .schedule(dto.getSchedule())
                .category(dto.getCategory())
                .platform(platform)
                .status(dto.getStatus() != null ? dto.getStatus() : VacancyStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .publishedAt(VacancyStatus.ACTIVE.equals(dto.getStatus()) ? LocalDateTime.now() : null)
                .build();

        vacancyRepository.save(vacancy);

        logService.log(currentUser, ActionType.CREATE_VACANCY,
                "Создана вакансия: " + vacancy.getTitle() + " (ID: " + vacancy.getId() + ") — " + vacancy.getStatus()
        );

        return toResponse(vacancy);
    }

    public VacancyResponseDTO updateVacancy(Long id, VacancyRequestDTO dto) {
        User currentUser = userService.getCurrentUser();
        checkIsHR(currentUser);

        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        var enterprise = enterpriseService.getByUserId(currentUser.getId());
        String city = hhAreaService.extractCity(enterprise.getAddress());
        String country = hhAreaService.extractCountry(enterprise.getAddress());
        var platform = hhAreaService.getPlatformByCountry(country);

        vacancy.setTitle(dto.getTitle());
        vacancy.setDescription(dto.getDescription());
        vacancy.setRequirements(dto.getRequirements());
        vacancy.setSkills(dto.getSkills());
        vacancy.setSalaryMin(dto.getSalaryMin());
        vacancy.setSalaryMax(dto.getSalaryMax());
        vacancy.setCurrency(dto.getCurrency());
        vacancy.setEmploymentType(dto.getEmploymentType());
        vacancy.setWorkFormat(dto.getWorkFormat());
        vacancy.setExperience(dto.getExperience());
        vacancy.setSchedule(dto.getSchedule());
        vacancy.setCategory(dto.getCategory());
        vacancy.setPlatform(platform);
        vacancy.setStatus(dto.getStatus());
        vacancy.setUpdatedAt(LocalDateTime.now());

        if (VacancyStatus.ACTIVE.equals(dto.getStatus()) && vacancy.getPublishedAt() == null) {
            vacancy.setPublishedAt(LocalDateTime.now());
        }

        vacancyRepository.save(vacancy);

        logService.log(currentUser, ActionType.UPDATE_VACANCY,
                "Обновлена вакансия: " + vacancy.getTitle() + " (ID: " + vacancy.getId() + ")"
        );

        return toResponse(vacancy);
    }


    public VacancyResponseDTO getVacancy(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        User currentUser = userService.getCurrentUser();
        logService.log(currentUser, ActionType.READ_VACANCY, "Просмотр вакансии ID: " + id);

        return toResponse(vacancy);
    }

    public List<VacancyResponseDTO> getAllVacancies() {
        return vacancyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<VacancyResponseDTO> getMyVacancies() {
        User currentUser = userService.getCurrentUser();
        checkIsHR(currentUser);

        return vacancyRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteVacancy(Long id) {
        User currentUser = userService.getCurrentUser();
        checkIsHR(currentUser);

        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        vacancyRepository.deleteById(id);

        logService.log(
                currentUser,
                ActionType.DELETE_VACANCY,
                "Удалена вакансия: " + vacancy.getTitle() + " (ID: " + id + ")"
        );
    }

    private VacancyResponseDTO toResponse(Vacancy v) {
        var enterprise = enterpriseService.getByUserId(v.getUser().getId());
        String city = hhAreaService.extractCity(enterprise.getAddress());
        String country = hhAreaService.extractCountry(enterprise.getAddress());
        var platform = hhAreaService.getPlatformByCountry(country);

        return VacancyResponseDTO.builder()
                .id(v.getId())
                .userId(v.getUser().getId())
                .title(v.getTitle())
                .description(v.getDescription())
                .requirements(v.getRequirements())
                .skills(v.getSkills())
                .keySkills(v.getSkills() != null ? List.of(v.getSkills().split("-")) : List.of())
                .salaryMin(v.getSalaryMin())
                .salaryMax(v.getSalaryMax())
                .currency(v.getCurrency())
                .city(city)
                .country(country)
                .hhAreaId(hhAreaService.getAreaId(country, city))
                .employmentType(v.getEmploymentType())
                .workFormat(v.getWorkFormat())
                .experience(v.getExperience())
                .schedule(v.getSchedule())
                .platform(platform)
                .status(v.getStatus())
                .publishedAt(VacancyStatus.ACTIVE.equals(v.getStatus()) ? v.getPublishedAt() : null)
                .externalId(VacancyStatus.ACTIVE.equals(v.getStatus()) ? v.getExternalId() : null)
                .createdAt(v.getCreatedAt())
                .updatedAt(v.getUpdatedAt() != null ? v.getUpdatedAt() : null)
                .build();
    }

}
