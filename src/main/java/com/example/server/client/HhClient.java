package com.example.server.client;

import com.example.server.dto.request.HhVacancyCreateRequest;
import com.example.server.dto.response.HhAuthTokenResponse;
import com.example.server.dto.response.HhVacancyCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HhClient {

    private final RestTemplate restTemplate;

    @Value("${hh.base-url}")
    private String baseUrl;
    public HhAuthTokenResponse exchangeCode(
            String code,
            String clientId,
            String clientSecret,
            String redirectUri
    ) {
        Map<String, String> body = Map.of(
                "grant_type", "authorization_code",
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "code", code
        );

        return restTemplate.postForObject(
                baseUrl + "/oauth/token",
                body,
                HhAuthTokenResponse.class
        );
    }
    public HhVacancyCreateResponse createVacancy(HhVacancyCreateRequest req, String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HhVacancyCreateRequest> entity = new HttpEntity<>(req, headers);

        return restTemplate.postForObject(
                baseUrl + "/vacancies",
                entity,
                HhVacancyCreateResponse.class
        );
    }
}

