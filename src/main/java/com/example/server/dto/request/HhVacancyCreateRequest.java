package com.example.server.dto.request;

import com.example.server.model.enums.ScheduleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class HhVacancyCreateRequest {

    private String name;
    private String description;
    private Area area;
    @JsonProperty("professional_roles")
    private List<Specialization> specializations;
    private Salary salary;
    private Experience experience;
    private Employment employment;
    private Schedule schedule;
    @JsonProperty("key_skills")
    private List<KeySkill>keySkills;
    private Contacts contacts;
    @JsonProperty("vacancy_properties")
    private VacancyProperties vacancyProperties;

    @Data
    public static class VacancyProperties {
        private List<Property> properties;
    }
    @Data
    public static class Property {
        @JsonProperty("property_type")
        private String propertyType;
    }
    @Data
    public static class Experience {
        private String id;
    }
    @Data
    public static class Employment {
        private String id;
    }

    @Data
    public static class Schedule {
        private String id;
    }

    @Data
    public static class Area {
        private String id;
    }

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

    @Data
    public static class KeySkill {
        private String name;
    }

    @Data
    public static class Specialization {
        private String id;
    }
}


