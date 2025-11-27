package com.example.server.client;

import com.example.server.dto.request.HhVacancyCreateRequest;
import com.example.server.dto.response.HhAreaResponse;
import com.example.server.dto.response.HhAuthTokenResponse;
import com.example.server.dto.response.HhProfessionalRolesResponse;
import com.example.server.dto.response.HhVacancyCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class HhClient {

    private final RestTemplate restTemplate;

    @Value("${hh.base-url}")
    private String baseUrl;

    public HhAuthTokenResponse exchangeCode(String code, String clientId, String clientSecret, String redirectUri) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(
                "https://api.hh.ru/token",
                request,
                HhAuthTokenResponse.class
        );
    }

    public ResponseEntity<HhAreaResponse[]> getAreas() {
        return restTemplate.getForEntity(baseUrl + "/areas", HhAreaResponse[].class);
    }

    public HhVacancyCreateResponse publish(HhVacancyCreateRequest req, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(req, headers);

        return restTemplate.postForObject(
                baseUrl + "/vacancies/drafts",
                entity,
                HhVacancyCreateResponse.class
        );
    }

    public HhProfessionalRolesResponse getProfessionalRoles() {
        return restTemplate.getForObject(
                baseUrl + "/professional_roles",
                HhProfessionalRolesResponse.class
        );
    }

}
