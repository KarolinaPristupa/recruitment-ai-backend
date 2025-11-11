package com.example.server.service;

import com.example.server.dto.UserRegistrationDTO;
import com.example.server.model.Enterprise;
import com.example.server.model.User;
import com.example.server.repository.EnterpriseRepository;
import com.example.server.repository.RoleRepository;
import com.example.server.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       EnterpriseRepository enterpriseRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerHR(UserRegistrationDTO dto) {
        User user = new User();
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        user.setRole(roleRepository.findByName("HR")
                .orElseThrow(() -> new RuntimeException("Role HR not found")));

        Enterprise enterprise = enterpriseRepository.findById(dto.getEnterpriseId())
                .orElseThrow(() -> new RuntimeException("Enterprise not found"));

        user.setEnterprise(enterprise);

        return userRepository.save(user);
    }
}

