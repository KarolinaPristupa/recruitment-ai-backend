package com.example.server.service;

import com.example.server.dto.request.HrProfileUpdateRequestDTO;
import com.example.server.dto.request.LoginRequestDTO;
import com.example.server.dto.request.UserRegistrationDTO;
import com.example.server.dto.response.HrProfileResponseDTO;
import com.example.server.model.Enterprise;
import com.example.server.model.User;
import com.example.server.model.enums.ActionType;
import com.example.server.repository.UserRepository;
import com.example.server.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HrProfileService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final LogService logService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public HrProfileResponseDTO getProfile() {
        User user = userService.getCurrentUser();
        Enterprise enterprise = user.getEnterprise();

        HrProfileResponseDTO dto = new HrProfileResponseDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        if (enterprise != null) {
            dto.setEnterpriseId(enterprise.getId());
            dto.setEnterpriseName(enterprise.getName());
            dto.setEnterpriseAddress(enterprise.getAddress());
            dto.setEnterpriseContactEmail(enterprise.getContactEmail());
            dto.setEnterpriseContactPhone(enterprise.getContactPhone());
        }

        return dto;
    }

    public Map<String, Object> updateProfile(UserRegistrationDTO request) {
        User user = userService.getCurrentUser();

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        userRepository.save(user);

        logService.log(user, ActionType.UPDATE_HR, "Обновлен профиль HR: " + user.getEmail());

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());

        return Map.of(
                "profile", getProfile(),
                "token", token
        );
    }
}
