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
        if (auth == null) {
            return ResponseEntity.status(403).body("Forbidden: user not authenticated");
        }

        User currentUser = (User) auth.getPrincipal();
        User savedUser = userService.registerHR(dto, currentUser);

        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().getName());

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setUsername(savedUser.getEmail());
        response.setRole(savedUser.getRole().getName());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/enterprise/admin/register")
    public ResponseEntity<?> registerEnterpriseAdmin(
            @RequestBody UserRegistrationDTO dto) {

        Enterprise enterprise = enterpriseService.findById(dto.getEnterpriseId());

        User admin = userService.registerEnterpriseAdmin(dto, enterprise);

        String token = jwtUtil.generateToken(admin.getEmail(), admin.getRole().getName());

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setUsername(admin.getEmail());
        response.setRole(admin.getRole().getName());

        return ResponseEntity.ok(response);
    }

}

