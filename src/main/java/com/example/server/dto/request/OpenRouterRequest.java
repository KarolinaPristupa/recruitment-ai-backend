package com.example.server.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class OpenRouterRequest {
    private String model;
    private List<Map<String, String>> messages;
}
