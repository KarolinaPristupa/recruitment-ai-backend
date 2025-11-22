package com.example.server.service;

import com.example.server.client.HhClient;
import com.example.server.dto.response.HhAuthTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HhAuthService {

    private final HhClient client;

    @Value("${hh.client-id}")
    private String clientId;

    @Value("${hh.client-secret}")
    private String clientSecret;

    @Value("${hh.redirect-uri}")
    private String redirectUri;

    public HhAuthTokenResponse exchangeCodeForToken(String code) {
        return client.exchangeCode(code, clientId, clientSecret, redirectUri);
    }
}

