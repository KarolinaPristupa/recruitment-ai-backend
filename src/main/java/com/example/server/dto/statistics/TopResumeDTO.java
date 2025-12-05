package com.example.server.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopResumeDTO {
    private Long id;
    private String applicantName;
    private double score;
    private double matchCount;
    private String fileUrl;
}
