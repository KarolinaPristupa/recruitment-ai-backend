package com.example.server.dto.response;

import com.example.server.dto.statistics.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyAnalyticsDTO {
    private List<ResponseDelayDTO> responseDelay;
    private List<SkillCountDTO> topSkills;
    private List<SkillGapDTO> skillGap;
    private VacancyStatsDTO stats;
    private List<TopResumeDTO> topResumes;
}

