package com.example.server.service;

import com.example.server.dto.request.EnterpriseWithAdminRegistrationDTO;
import com.example.server.dto.response.UserAccountResponseDTO;
import com.example.server.exception.EnterpriseEmailExistsException;
import com.example.server.exception.EnterprisePhoneExistsException;
import com.example.server.model.Enterprise;
import com.example.server.model.User;
import com.example.server.model.enums.ActionType;
import com.example.server.repository.EnterpriseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final UserService userService;

    private final LogService logService;

    public EnterpriseService(EnterpriseRepository enterpriseRepository, UserService userService, LogService logService) {
        this.enterpriseRepository = enterpriseRepository;
        this.userService = userService;
        this.logService = logService;
    }

    @Transactional
    public User registerEnterpriseWithAdmin(EnterpriseWithAdminRegistrationDTO dto) {
        String contactEmail = dto.getContactEmail();
        String contactPhone = dto.getContactPhone();

        if (enterpriseRepository.existsByContactEmail(contactEmail)) {
            throw new EnterpriseEmailExistsException(contactEmail);
        }

        if (contactPhone != null && !contactPhone.isBlank()
                && enterpriseRepository.existsByContactPhone(contactPhone)) {
            throw new EnterprisePhoneExistsException(contactPhone);
        }
        Enterprise enterprise = new Enterprise();
        enterprise.setName(dto.getName());
        enterprise.setAddress(dto.getAddress());
        enterprise.setContactEmail(dto.getContactEmail());
        enterprise.setContactPhone(dto.getContactPhone());
        enterprise = enterpriseRepository.save(enterprise);

        User admin = userService.createEnterpriseAdmin(dto, enterprise);
        return admin;
    }

    public Enterprise findById(Long id) {
        return enterpriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enterprise not found with id: " + id));
    }

    public Enterprise getByUserId(Long userId) {
        User user = userService.getById(userId);
        Enterprise enterprise = enterpriseRepository.findById(user.getEnterprise().getId())
                .orElseThrow(() -> new RuntimeException("Enterprise not found for user id: " + userId));
        return enterprise;
    }

    public boolean existsByEmail(String email) {
        return enterpriseRepository.existsByContactEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return enterpriseRepository.existsByContactPhone(phone);
    }

    @Transactional
    public Map<String, Object> updateEnterprise(UserAccountResponseDTO request) {
        User user = userService.getCurrentUser();

        if (user.getEnterprise() == null) {
            throw new RuntimeException("User is not linked to an enterprise");
        }

        Enterprise enterprise = enterpriseRepository.findById(user.getEnterprise().getId())
                .orElseThrow(() -> new RuntimeException("Enterprise not found"));

        if (request.getEnterpriseContactEmail() != null &&
                !request.getEnterpriseContactEmail().equals(enterprise.getContactEmail()) &&
                enterpriseRepository.existsByContactEmail(request.getEnterpriseContactEmail())) {

            throw new EnterpriseEmailExistsException(request.getEnterpriseContactEmail());
        }

        if (request.getEnterpriseContactPhone() != null &&
                !request.getEnterpriseContactPhone().equals(enterprise.getContactPhone()) &&
                enterpriseRepository.existsByContactPhone(request.getEnterpriseContactPhone())) {

            throw new EnterprisePhoneExistsException(request.getEnterpriseContactPhone());
        }

        enterprise.setName(request.getEnterpriseName());
        enterprise.setAddress(request.getEnterpriseAddress());
        enterprise.setContactEmail(request.getEnterpriseContactEmail());
        enterprise.setContactPhone(request.getEnterpriseContactPhone());

        enterpriseRepository.save(enterprise);

        logService.log(
                user,
                ActionType.UPDATE_ENTERPRISE,
                "Обновлены данные предприятия: " + enterprise.getName()
        );

        return Map.of(
                "profile", userService.getUser()
        );
    }

}
