package com.example.server.client;

import com.example.server.config.AnalysisConfig;
import com.example.server.dto.request.OpenRouterRequest;
import com.example.server.dto.response.OpenRouterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenRouterClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public String analyze(String prompt, String apiKey) {

        OpenRouterRequest req = OpenRouterRequest.builder()
                .model(AnalysisConfig.MODEL)
                .messages(List.of(
                        Map.of("role", "user", "content", prompt)
                ))
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OpenRouterRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<OpenRouterResponse> response =
                restTemplate.exchange(
                        AnalysisConfig.OPENROUTER_URL,
                        HttpMethod.POST,
                        entity,
                        OpenRouterResponse.class
                );

        return response.getBody()
                .getChoices()[0]
                .getMessage()
                .getContent();
    }
}
