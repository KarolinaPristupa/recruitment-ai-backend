package com.example.server.service;

import com.example.server.dto.statistics.*;
import com.example.server.model.ExternalVacancy;
import com.example.server.model.ResponseAnalysis;
import com.example.server.repository.ResumeAnalysisRepository;
import com.example.server.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VacancyAnalyticsService {

    private final ExternalVacancyService vacancyService;
    private final ExternalResponseService responseService;
    private final ResumeAnalysisRepository analysisRepository;

    public List<ResponseDelayDTO> getResponseDelay(Long vacancyId) {
        ExternalVacancy v = vacancyService.getById(vacancyId);

        return responseService.getByExternalVacancy(v).stream()
                .map(r -> new ResponseDelayDTO(
                        r.getId(),
                        Duration.between(v.getPublishedAt(), r.getDateReceived()).toHours()
                ))
                .toList();
    }

    public List<SkillCountDTO> getTopSkills(Long vacancyId) {
        List<ResponseAnalysis> list = analysisRepository.findByVacancyId(vacancyId);

        Map<String, Long> freq = new HashMap<>();

        for (ResponseAnalysis ra : list) {
            for (String skill : JsonUtils.parseSkillList(ra.getSkillsJson())) {
                freq.merge(skill.toLowerCase(), 1L, Long::sum);
            }
        }

        return freq.entrySet().stream()
                .sorted(Map.Entry.<String,Long>comparingByValue().reversed())
                .map(e -> new SkillCountDTO(e.getKey(), e.getValue()))
                .toList();
    }
    public List<SkillGapDTO> getSkillGap(Long vacancyId) {
        ExternalVacancy vacancy = vacancyService.getById(vacancyId);

        List<String> vacancySkills = JsonUtils
                .parseSkillList(vacancy.getVacancy().getSkills())
                .stream().map(String::toLowerCase)
                .toList();

        List<ResponseAnalysis> list = analysisRepository.findByVacancyId(vacancyId);

        Map<String, Long> gap = new HashMap<>();

        for (ResponseAnalysis ra : list) {
            for (String skill : JsonUtils.parseSkillList(ra.getSkillsJson())) {
                String s = skill.toLowerCase();
                if (!vacancySkills.contains(s)) {
                    gap.merge(s, 1L, Long::sum);
                }
            }
        }

        return gap.entrySet().stream()
                .sorted(Map.Entry.<String,Long>comparingByValue().reversed())
                .map(e -> new SkillGapDTO(e.getKey(), e.getValue()))
                .toList();
    }

    public VacancyStatsDTO getStats(Long vacancyId) {
        List<ResponseAnalysis> list = analysisRepository.findByVacancyId(vacancyId);

        double avgScore = list.stream()
                .mapToDouble(ResponseAnalysis::getScore).average().orElse(0);

        double avgMatch = list.stream()
                .mapToDouble(ResponseAnalysis::getMatchPercent).average().orElse(0);

        return new VacancyStatsDTO(avgScore, avgMatch);
    }
    public List<TopResumeDTO> getTopResumes(Long vacancyId, Pageable limit) {
        return analysisRepository.findTopByVacancyId(vacancyId, limit).stream()
                .map(ra -> new TopResumeDTO(
                        ra.getExternalResponse().getId(),
                        ra.getExternalResponse().getApplicantName(),
                        ra.getScore(),
                        ra.getMatchPercent(),
                        ra.getExternalResponse().getFileUrl()
                ))
                .toList();
    }
}
