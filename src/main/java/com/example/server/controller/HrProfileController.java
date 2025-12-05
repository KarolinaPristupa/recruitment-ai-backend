package com.example.server.controller;

import com.example.server.dto.request.HrProfileUpdateRequestDTO;
import com.example.server.dto.request.UserRegistrationDTO;
import com.example.server.dto.response.HrProfileResponseDTO;
import com.example.server.service.HrProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hr/account")
@RequiredArgsConstructor
public class HrProfileController {

    private final HrProfileService hrProfileService;

    @GetMapping
    public HrProfileResponseDTO getProfile() {
        return hrProfileService.getProfile();
    }

    @PutMapping
    public Map<String, Object> updateProfile(@RequestBody UserRegistrationDTO request) {
        return hrProfileService.updateProfile(request);
    }

}
