package com.example.server.controller;

import com.example.server.dto.request.LoginRequestDTO;
import com.example.server.dto.response.AuthResponseDTO;
import com.example.server.model.User;
import com.example.server.service.UserService;
import com.example.server.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public LoginController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto) {

        User user = userService.authenticate(dto.getEmail(), dto.getPassword());

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().getName()
        );

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setUsername(user.getEmail());
        response.setRole(user.getRole().getName());

        return ResponseEntity.ok(response);
    }
}
