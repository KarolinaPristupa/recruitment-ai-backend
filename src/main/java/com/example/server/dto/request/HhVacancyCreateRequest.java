package com.example.server.dto.request;

import lombok.Data;

@Data
public class HhVacancyCreateRequest {
    private String name;
    private String description;
    private Integer area;
    private Salary salary;

    @Data
    public static class Salary {
        private Integer from;
        private Integer to;
        private String currency = "RUR";
    }
}

