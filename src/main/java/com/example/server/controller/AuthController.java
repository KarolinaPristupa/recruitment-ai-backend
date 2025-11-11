package com.example.server.controller;

import com.example.server.dto.AuthResponseDTO;
import com.example.server.dto.EnterpriseRegistrationDTO;
import com.example.server.dto.UserRegistrationDTO;
import com.example.server.model.Enterprise;
import com.example.server.model.User;
import com.example.server.service.EnterpriseService;
import com.example.server.service.UserService;
import com.example.server.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final EnterpriseService enterpriseService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(EnterpriseService enterpriseService, UserService userService, JwtUtil jwtUtil) {
        this.enterpriseService = enterpriseService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/enterprise/register")
    public ResponseEntity<?> registerEnterprise(@RequestBody EnterpriseRegistrationDTO dto) {
        Enterprise enterprise = enterpriseService.register(dto);
        return ResponseEntity.ok(enterprise);
    }

    @PostMapping("/hr/register")
    public ResponseEntity<?> registerHR(@RequestBody UserRegistrationDTO dto, Authentication auth) {
        User currentUser = userService.findByEmail(auth.getName());
        User user = userService.registerHR(dto, currentUser);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setUsername(user.getEmail());
        response.setRole(user.getRole().getName());

        return ResponseEntity.ok(response);
    }
}

