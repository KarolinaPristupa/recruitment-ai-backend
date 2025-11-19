package com.example.server.service;

import com.example.server.dto.EnterpriseWithAdminRegistrationDTO;
import com.example.server.model.Enterprise;
import com.example.server.model.User;
import com.example.server.repository.EnterpriseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final UserService userService;

    public EnterpriseService(EnterpriseRepository enterpriseRepository, UserService userService) {
        this.enterpriseRepository = enterpriseRepository;
        this.userService = userService;
    }

    @Transactional
    public User registerEnterpriseWithAdmin(EnterpriseWithAdminRegistrationDTO dto) {
        String contactEmail = dto.getContactEmail();
        String contactPhone = dto.getContactPhone();

        if (enterpriseRepository.existsByContactEmail(contactEmail)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email компании уже зарегистрирован: " + contactEmail
            );
        }

        if (contactPhone != null && !contactPhone.isBlank()
                && enterpriseRepository.existsByContactPhone(contactPhone)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Телефон компании уже зарегистрирован: " + contactPhone
            );
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
}