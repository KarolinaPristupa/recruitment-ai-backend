package com.example.server.service;

import com.example.server.client.OpenRouterClient;
import com.example.server.model.*;
import com.example.server.model.enums.ActionType;
import com.example.server.repository.ResponseAnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeAnalysisService {

    private final OpenRouterClient aiClient;
    private final ResponseAnalysisRepository analysisRepository;
    private final LogService logService;

    @Value("${openrouter.api-key}")
    private String aiApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void analyzeAll(ExternalVacancy vacancy, List<ExternalResponse> responses, User currentUser) {
        for (ExternalResponse r : responses) {
            boolean exists = analysisRepository.existsByExternalResponse(r);
            if (!exists) analyzeSingle(r, vacancy.getVacancy(), currentUser);
        }
    }

    public ResponseAnalysis analyzeSingle(ExternalResponse response, Vacancy vacancy, User currentUser) {
        String resumeText = loadResumeAsText(response.getFileUrl());
        if (resumeText.isBlank()) {
            log.warn("Резюме пустое: {}", response.getFileUrl());
            resumeText = "Resume details not available.";
        }

        String prompt = """
                Analyze a resume for job matching.

                Vacancy:
                %s

                Resume:
                %s

                Return JSON with fields:
                {
                  "score": number (0-100),
                  "skills": [...],
                  "experienceYears": number,
                  "seniority": "junior|middle|senior",
                  "matchPercent": number
                }
                """.formatted(
                vacancy.getTitle() + "\n" + vacancy.getDescription() + "\n" + vacancy.getRequirements(),
                resumeText
        );

        String aiResponse;
        try {
            aiResponse = aiClient.analyze(prompt, aiApiKey);
        } catch (Exception e) {
            log.error("Ошибка при вызове OpenRouter: {}", e.getMessage(), e);
            aiResponse = "{\"score\":0,\"skills\":[],\"experienceYears\":0,\"seniority\":\"junior\",\"matchPercent\":0}";
        }

        ResponseAnalysis analysis = saveAnalysis(response, aiResponse);
        logService.log(currentUser, ActionType.ANALYZE_RESPONSE,
                "Проанализирован отклик ID " + response.getId() + " по вакансии '" + vacancy.getTitle() + "' (ID: " + vacancy.getId() + ")");
        return analysis;
    }

    private String loadResumeAsText(String pathToFile) {
        try {
            Path filePath = Path.of("src/main/resources" + pathToFile);
            try (PDDocument document = PDDocument.load(filePath.toFile())) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document).trim();
            }
        } catch (Exception e) {
            log.error("Ошибка извлечения текста из резюме: {}", pathToFile, e);
            return "";
        }
    }

    private ResponseAnalysis saveAnalysis(ExternalResponse response, String json) {
        String cleanedJson = json.replaceAll("(?s)```json\\s*", "").replaceAll("```", "").trim();

        double score = 0;
        double experienceYears = 0;
        String seniority = "junior";
        double matchPercent = 0;
        List<String> skills = List.of();

        try {
            JsonNode root = objectMapper.readTree(cleanedJson);
            if (root.has("score")) score = root.get("score").asDouble(0);
            if (root.has("experienceYears")) experienceYears = root.get("experienceYears").asDouble(0);
            if (root.has("seniority")) seniority = root.get("seniority").asText("junior");
            if (root.has("matchPercent")) matchPercent = root.get("matchPercent").asDouble(0);
            if (root.has("skills") && root.get("skills").isArray()) {
                skills = objectMapper.convertValue(
                        root.get("skills"),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
                );
            }
        } catch (Exception e) {
            log.warn("Не удалось распарсить JSON от AI: {}", cleanedJson, e);
        }

        String skillsJson;
        try {
            skillsJson = objectMapper.writeValueAsString(skills);
        } catch (Exception e) {
            log.warn("Не удалось сериализовать skills в JSON", e);
            skillsJson = "[]";
        }

        ResponseAnalysis analysis = ResponseAnalysis.builder()
                .externalResponse(response)
                .analysisJson(cleanedJson)
                .score(score)
                .experienceYears(experienceYears)
                .matchPercent(matchPercent)
                .seniority(seniority)
                .skillsJson(skillsJson)
                .createdAt(LocalDateTime.now())
                .build();

        return analysisRepository.save(analysis);
    }


    public List<ResponseAnalysis> getTopResults(ExternalVacancy vacancy, int limit) {
        return analysisRepository.findTopByVacancy(vacancy.getId(), limit);
    }
}
