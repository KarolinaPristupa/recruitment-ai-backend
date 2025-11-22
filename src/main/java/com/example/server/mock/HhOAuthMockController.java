package com.example.server.mock;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/mock-hh")
public class HhOAuthMockController {
    @GetMapping("/oauth/authorize")
    public void authorize(
            @RequestParam String client_id,
            @RequestParam String redirect_uri,
            @RequestParam(required = false) String state,
            HttpServletResponse response
    ) throws IOException {

        String code = "mock-code-123";

        String redirect = redirect_uri + "?code=" + code;
        if (state != null) redirect += "&state=" + state;

        response.sendRedirect(redirect);
    }

    @PostMapping("/oauth/token")
    public Map<String, Object> token(@RequestBody Map<String, String> body) {

        String code = body.get("code");
        if (!"mock-code-123".equals(code)) {
            return Map.of(
                    "error", "invalid_grant",
                    "error_description", "unknown code"
            );
        }

        return Map.of(
                "access_token", "mock-access-token",
                "refresh_token", "mock-refresh-token",
                "expires_in", 3600,
                "token_type", "bearer"
        );
    }
}
