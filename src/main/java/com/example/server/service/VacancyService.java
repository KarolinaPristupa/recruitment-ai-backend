package com.example.server.service;

import com.example.server.dto.request.VacancyRequestDTO;
import com.example.server.dto.response.VacancyResponseDTO;
import com.example.server.model.User;
import com.example.server.model.Vacancy;
import com.example.server.model.enums.ActionType;
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

    private void checkIsHR(User user) {
        if (!"HR".equals(user.getRole().getName())) {
            throw new RuntimeException("Access denied: only HR can create or manage vacancies");
        }
    }

    public VacancyResponseDTO createVacancy(VacancyRequestDTO dto) {
        User currentUser = userService.getCurrentUser();

        checkIsHR(currentUser);

        Vacancy vacancy = Vacancy.builder()
                .user(currentUser)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .requirements(dto.getRequirements())
                .salaryMin(dto.getSalaryMin())
                .salaryMax(dto.getSalaryMax())
                .status(dto.getStatus())
                .externalIds(dto.getExternalIds())
                .publishedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        vacancyRepository.save(vacancy);

        logService.log(
                currentUser,
                ActionType.CREATE_VACANCY,
                "Создана вакансия: " + vacancy.getTitle() +
                        " (ID: " + vacancy.getId() + ")"
        );

        return toResponse(vacancy);
    }

    public VacancyResponseDTO getVacancy(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        User currentUser = userService.getCurrentUser();

        logService.log(
                currentUser,
                ActionType.READ_VACANCY,
                "Просмотр вакансии ID: " + id
        );

        return toResponse(vacancy);
    }

    public List<VacancyResponseDTO> getAllVacancies() {
        User currentUser = userService.getCurrentUser();

        logService.log(
                currentUser,
                ActionType.READ_VACANCY,
                "Просмотр списка всех вакансий"
        );

        return vacancyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public List<VacancyResponseDTO> getMyVacancies() {
        User currentUser = userService.getCurrentUser();

        checkIsHR(currentUser);

        logService.log(
                currentUser,
                ActionType.READ_VACANCY,
                "Просмотр списка своих вакансий"
        );

        return vacancyRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public VacancyResponseDTO updateVacancy(Long id, VacancyRequestDTO dto) {
        User currentUser = userService.getCurrentUser();

        checkIsHR(currentUser);

        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        vacancy.setTitle(dto.getTitle());
        vacancy.setDescription(dto.getDescription());
        vacancy.setRequirements(dto.getRequirements());
        vacancy.setSalaryMin(dto.getSalaryMin());
        vacancy.setSalaryMax(dto.getSalaryMax());
        vacancy.setStatus(dto.getStatus());
        vacancy.setExternalIds(dto.getExternalIds());

        vacancyRepository.save(vacancy);

        logService.log(
                currentUser,
                ActionType.UPDATE_VACANCY,
                "Обновлена вакансия: " + vacancy.getTitle() +
                        " (ID: " + vacancy.getId() + ")"
        );

        return toResponse(vacancy);
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
                "Удалена вакансия: " + vacancy.getTitle() +
                        " (ID: " + id + ")"
        );
    }

    private VacancyResponseDTO toResponse(Vacancy v) {
        return VacancyResponseDTO.builder()
                .id(v.getId())
                .userId(v.getUser().getId())
                .title(v.getTitle())
                .description(v.getDescription())
                .requirements(v.getRequirements())
                .salaryMin(v.getSalaryMin())
                .salaryMax(v.getSalaryMax())
                .status(v.getStatus())
                .publishedAt(v.getPublishedAt())
                .externalIds(v.getExternalIds())
                .createdAt(v.getCreatedAt())
                .build();
    }
}
