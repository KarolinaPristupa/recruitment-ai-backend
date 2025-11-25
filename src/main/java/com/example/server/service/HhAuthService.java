package com.example.server.service;

import com.example.server.client.HhClient;
import com.example.server.dto.response.HhAuthTokenResponse;
import com.example.server.model.HhToken;
import com.example.server.model.User;
import com.example.server.model.enums.ActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HhAuthService {

    private final HhClient client;
    private final HhTokenService tokenService;
    private final LogService logService;

    @Value("${hh.client-id}")
    private String clientId;

    @Value("${hh.client-secret}")
    private String clientSecret;

    @Value("${hh.redirect-uri}")
    private String redirectUri;

    public HhAuthTokenResponse exchangeCodeForTokenForHr(String code, Long hrId) {
        var token = client.exchangeCode(code, clientId, clientSecret, redirectUri);

        tokenService.saveTokenForHr(hrId, token);

        User hr = tokenService.getTokenByHrId(hrId).getHr();
        logService.log(hr, ActionType.HH_LOGIN, "HR вошёл на HH: " + hr.getEmail());
        logService.log(hr, ActionType.HH_TOKEN_SAVED, "Сохранён токен HH для HR: " + hr.getEmail());

        return token;
    }

    public HhToken getTokenForHr(User hr) {
        return tokenService.getTokenByHr(hr);
    }

    public boolean hasTokenForHr(Long hrId) {
        return tokenService.hasTokenForHr(hrId);
    }

    public String getLoginUrl(Long hrId) {
        return "https://hh.ru/oauth/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&state=" + hrId;
    }
}
