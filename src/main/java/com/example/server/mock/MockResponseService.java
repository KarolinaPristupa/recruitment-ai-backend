package com.example.server.mock;

import com.example.server.dto.response.ExternalResponseDto;
import com.example.server.model.ExternalResponse;
import com.example.server.model.ExternalVacancy;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MockResponseService {

    private final ObjectMapper objectMapper;

    public MockResponseService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<ExternalResponse> loadResponsesFromJson(ExternalVacancy externalVacancy) {
        try (InputStream is = getClass().getResourceAsStream("/responses.json")) {
            if (is == null) return List.of();

            List<ExternalResponseDto> dtos = objectMapper.readValue(is, new TypeReference<>() {});

            return dtos.stream()
                    .map(dto -> ExternalResponse.builder()
                            .externalVacancy(externalVacancy)
                            .resumeId(dto.getResumeId())
                            .applicantName(dto.getApplicantName())
                            .messageText(dto.getMessageText())
                            .fileUrl(dto.getFileUrl())
                            .dateApplied(dto.getDateApplied())
                            .dateReceived(LocalDateTime.now())
                            .status(dto.getStatus())
                            .externalResponseId(dto.getExternalResponseId())
                            .build()
                    ).toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
