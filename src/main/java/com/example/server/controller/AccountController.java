package com.example.server.controller;

import com.example.server.dto.request.UserRegistrationDTO;
import com.example.server.dto.response.UserAccountResponseDTO;
import com.example.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

    @GetMapping
    public UserAccountResponseDTO getUser() {
        return userService.getUser();
    }

    @PutMapping
    public Map<String, Object> updateUser(@RequestBody UserRegistrationDTO request) {
        return userService.updateUser(request);
    }

}
