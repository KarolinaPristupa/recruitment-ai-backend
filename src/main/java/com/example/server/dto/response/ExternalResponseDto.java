package com.example.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExternalResponseDto {

    private String resumeId;
    private String applicantName;
    private String messageText;
    private String fileUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateApplied;

    private String status;
    private String externalResponseId;
}
