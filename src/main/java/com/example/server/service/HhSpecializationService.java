package com.example.server.service;

import com.example.server.client.HhClient;
import com.example.server.dto.response.HhProfessionalRolesResponse;
import com.example.server.model.enums.HhSpecialization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HhSpecializationService {

    private final HhClient hhClient;

    public String getSpecializationCode(String vacancyTitle) {
        if (vacancyTitle == null) return null;

        String id = HhSpecialization.getIdByName(vacancyTitle);
        if (id != null) return id;

        var rolesResponse = hhClient.getProfessionalRoles();

        return findBestMatch(vacancyTitle, rolesResponse);
    }

    private String findBestMatch(String title, HhProfessionalRolesResponse rolesResponse) {
        if (title == null) return null;

        // оставляем первые 2 значимых слова
        String[] titleWords = title.toLowerCase()
                .replaceAll("[^a-zа-я0-9 ]", " ")
                .split("\\s+");

        List<String> keywords = Arrays.stream(titleWords)
                .filter(w -> w.length() > 2)
                .limit(3)
                .toList();

        String bestId = null;
        int bestScore = 0;

        for (var category : rolesResponse.getCategories()) {
            for (var role : category.getRoles()) {
                int score = 0;
                String roleName = role.getName().toLowerCase();

                for (String kw : keywords) {
                    if (roleName.contains(kw)) score++;
                }

                if (score > bestScore) {
                    bestScore = score;
                    bestId = role.getId();
                }
            }
        }

        return bestId;
    }
}
