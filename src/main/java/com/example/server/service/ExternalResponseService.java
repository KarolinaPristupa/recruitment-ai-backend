package com.example.server.service;

import com.example.server.client.HhClient;
import com.example.server.mock.MockResponseService;
import com.example.server.model.ExternalResponse;
import com.example.server.model.ExternalVacancy;
import com.example.server.model.HhToken;
import com.example.server.model.User;
import com.example.server.model.enums.ActionType;
import com.example.server.repository.ExternalResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalResponseService {

    private final ExternalResponseRepository responseRepository;
    private final HhClient hhClient;
    private final MockResponseService mockResponseService;
    private final HhTokenService hhTokenService;
    private final LogService logService;

    public List<ExternalResponse> fetchResponses(ExternalVacancy externalVacancy, User currentUser) {
        try {
            HhToken token = hhTokenService.getTokenByHr(externalVacancy.getVacancy().getUser());

            List<ExternalResponse> hhResponses = hhClient.getResponses(
                    externalVacancy.getExternalId(),
                    token.getAccessToken()
            );

            List<ExternalResponse> newResponses = hhResponses.stream()
                    .filter(r -> !responseRepository.existsByExternalVacancyAndExternalResponseId(
                            externalVacancy,
                            r.getExternalResponseId()
                    ))
                    .peek(r -> {
                        r.setExternalVacancy(externalVacancy);
                        r.setDateReceived(LocalDateTime.now());
                    })
                    .toList();

            responseRepository.saveAll(newResponses);

            logService.log(
                    currentUser,
                    ActionType.FETCH_RESPONSES,
                    "Загружены отклики с HH для вакансии: " +
                            externalVacancy.getVacancy().getTitle() +
                            " (ID: " + externalVacancy.getId() + ")"
            );

            return responseRepository.findByExternalVacancy(externalVacancy);

        } catch (HttpClientErrorException ex) {
            log.error("HH API вернул ошибку: {} BODY: {}", ex.getStatusCode(), ex.getResponseBodyAsString());

            List<ExternalResponse> mockResponses = mockResponseService.loadResponsesFromJson(externalVacancy);

            List<ExternalResponse> newMocks = mockResponses.stream()
                    .filter(r -> !responseRepository.existsByExternalVacancyAndExternalResponseId(
                            externalVacancy,
                            r.getExternalResponseId()
                    ))
                    .peek(r -> {
                        r.setExternalVacancy(externalVacancy);
                        r.setDateReceived(LocalDateTime.now());
                    })
                    .toList();

            responseRepository.saveAll(newMocks);

            logService.log(
                    currentUser,
                    ActionType.FETCH_RESPONSES,
                    "Загружены отклики с HH для вакансии: " +
                            externalVacancy.getVacancy().getTitle() +
                            " (ID: " + externalVacancy.getId() + ")"
            );

            return responseRepository.findByExternalVacancy(externalVacancy);
        }
    }


    public List<ExternalResponse> getByExternalVacancy(ExternalVacancy externalVacancy) {
        return responseRepository.findByExternalVacancy(externalVacancy);
    }

    public ExternalResponse getById(Long id) {
        return responseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Response not found"));
    }

}
