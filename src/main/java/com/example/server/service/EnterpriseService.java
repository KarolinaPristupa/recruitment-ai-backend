package com.example.server.service;

import com.example.server.dto.EnterpriseRegistrationDTO;
import com.example.server.model.Enterprise;
import com.example.server.repository.EnterpriseRepository;
import org.springframework.stereotype.Service;

@Service
public class EnterpriseService {
    private final EnterpriseRepository enterpriseRepository;

    public EnterpriseService(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    public Enterprise register(EnterpriseRegistrationDTO dto) {
        Enterprise enterprise = new Enterprise();
        enterprise.setName(dto.getName());
        enterprise.setAddress(dto.getAddress());
        enterprise.setContactEmail(dto.getContactEmail());
        enterprise.setContactPhone(dto.getContactPhone());
        return enterpriseRepository.save(enterprise);
    }

    public Enterprise findById(Long id) {
        return enterpriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enterprise not found with id: " + id));
    }
}
