package com.example.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.server.service.HhAuthService;

@RestController
@RequestMapping("/api/hh/oauth")
@RequiredArgsConstructor
public class HhAuthController {

    private final HhAuthService hhAuthService;

    @GetMapping("/callback")
    public ResponseEntity<?> callback(
            @RequestParam String code,
            @RequestParam(required = false) String state
    ) {
        return ResponseEntity.ok(hhAuthService.exchangeCodeForToken(code));
    }
}
