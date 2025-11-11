package com.example.server.service;

import com.example.server.dto.UserRegistrationDTO;
import com.example.server.model.Enterprise;
import com.example.server.model.User;
import com.example.server.model.enums.ActionType;
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
    private final LogService logService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       EnterpriseRepository enterpriseRepository, PasswordEncoder passwordEncoder,
                       LogService logService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User registerHR(UserRegistrationDTO dto, User currentUser) {
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

        User savedUser = userRepository.save(user);

        logService.log(currentUser, ActionType.CREATE_USER,
                "Создан HR: " + savedUser.getEmail() +
                        (savedUser.getEnterprise() != null ? ", Enterprise: " + savedUser.getEnterprise().getName() : ""));

        return savedUser;
    }
}


