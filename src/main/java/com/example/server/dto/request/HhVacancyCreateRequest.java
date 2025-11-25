package com.example.server.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class HhVacancyCreateRequest {

    private String name;
    private String description;
    private Integer area;

    private Salary salary;
    private String experience;      // noExperience, between1And3, etc.
    private String employment;      // full, part
    private String schedule;        // fullDay, remote, flexible
    private List<String> keySkills; // список навыков
    private Contacts contacts;

    @Data
    public static class Salary {
        private Integer from;
        private Integer to;
        private String currency = "RUR";
        private Boolean gross = true;
    }

    @Data
    public static class Contacts {
        private String name;
        private String email;
        private String phone;
    }
}


