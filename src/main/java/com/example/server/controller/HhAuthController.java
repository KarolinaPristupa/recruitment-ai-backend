package com.example.server.controller;

import com.example.server.service.HhTokenService;
import com.example.server.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.server.service.HhAuthService;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/hh/oauth")
@RequiredArgsConstructor
public class HhAuthController {

    private final HhAuthService hhAuthService;
    private final UserService userService;
    private final HhTokenService hhTokenService;

    @Value("${hh.client-id}")
    private String clientId;

    @Value("${hh.redirect-uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public void callback(
            @RequestParam String code,
            @RequestParam Long state,
            HttpServletResponse response
    ) throws IOException {
        var hrId = state;
        var tokenResponse = hhAuthService.exchangeCodeForTokenForHr(code, hrId);

        response.sendRedirect("http://localhost:5173/v-ai/#/hr/vacancies");
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        Long hrId = userService.getCurrentUser().getId();

        String url = "https://hh.ru/oauth/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&state=" + hrId;

        response.sendRedirect(url);
    }


    @GetMapping("/check-token")
    public ResponseEntity<Boolean> checkToken() {
        try {
            var hr = userService.getCurrentUser();
            boolean exists = hhTokenService.hasTokenForHr(hr.getId());
            return ResponseEntity.ok(exists);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/login-url")
    public ResponseEntity<String> loginUrl() {
        Long hrId = userService.getCurrentUser().getId();

        String url = "https://hh.ru/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&state=" + hrId;

        return ResponseEntity.ok(url);
    }

}
