package com.example.server.service;

import com.example.server.dto.EnterpriseWithAdminRegistrationDTO;
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
    EnterpriseRepository enterpriseRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       LogService logService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User registerHR(UserRegistrationDTO dto, User currentUser) {
        Enterprise enterprise = enterpriseRepository.findById(dto.getEnterpriseId())
                .orElseThrow(() -> new RuntimeException("Enterprise not found"));

        User user = createUser(dto, "HR", enterprise);
        User savedUser = userRepository.save(user);

        logService.log(currentUser, ActionType.CREATE_HR,
                "Создан HR: " + savedUser.getEmail() + ", Enterprise: " + enterprise.getName());

        return savedUser;
    }

    User createEnterpriseAdmin(EnterpriseWithAdminRegistrationDTO dto, Enterprise enterprise) {
        User admin = new User();
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        admin.setEnterprise(enterprise);
        admin.setRole(roleRepository.findByName("ENT_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ENT_ADMIN not found")));

        User savedAdmin = userRepository.save(admin);

        logService.log(savedAdmin, ActionType.CREATE_ENTERPRISE,
                "Создана компания: " + enterprise.getName());

        logService.log(savedAdmin, ActionType.CREATE_ADMIN,
                "Создан администратор компании: " + savedAdmin.getEmail() +
                        " (компания: " + enterprise.getName() + ")");
        return savedAdmin;
    }

    private User createUser(UserRegistrationDTO dto, String roleName, Enterprise enterprise) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setEnterprise(enterprise);
        user.setRole(roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found")));
        return user;
    }
}