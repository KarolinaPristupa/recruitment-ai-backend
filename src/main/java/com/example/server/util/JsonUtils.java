package com.example.server.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<String> parseSkillList(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return mapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
