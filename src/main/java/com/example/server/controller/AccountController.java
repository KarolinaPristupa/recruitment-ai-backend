package com.example.server.controller;

import com.example.server.dto.request.UserRegistrationDTO;
import com.example.server.dto.response.UserAccountResponseDTO;
import com.example.server.service.EnterpriseService;
import com.example.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final EnterpriseService enterpriseService;

    @GetMapping
    public UserAccountResponseDTO getUser() {
        return userService.getUser();
    }

    @PutMapping
    public Map<String, Object> updateUser(@RequestBody UserRegistrationDTO request) {
        return userService.updateUser(request);
    }

    @PutMapping("/enterprise")
    public Map<String, Object> updateEnterprise(@RequestBody UserAccountResponseDTO request) {
        return enterpriseService.updateEnterprise(request);
    }

}
