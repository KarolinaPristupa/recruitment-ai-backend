package com.example.server.service;

import com.example.server.client.HhClient;
import com.example.server.dto.response.HhAreaResponse;
import com.example.server.model.enums.HhRegion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HhAreaService {

    private final HhClient hhClient;

    public String extractCountry(String address) {
        if (address == null) return null;
        return address.split(",")[0].trim();
    }

    public String extractCity(String address) {
        if (address == null) return null;
        String[] parts = address.split(",");
        if (parts.length < 2) return null;
        String city = parts[1].trim();
        if (city.startsWith("г.")) {
            city = city.replace("г.", "").trim();
        }
        return city;
    }

    public Integer getAreaId(String country, String city) {
        if (country == null || city == null) return null;

        Integer localId = HhRegion.getIdByCity(city);
        if (localId != null) return localId;

        try {
            ResponseEntity<HhAreaResponse[]> response = hhClient.getAreas();
            if (response.getBody() != null) {
                for (HhAreaResponse countryArea : response.getBody()) {
                    if (countryArea.getName().equalsIgnoreCase(country.trim())) {
                        Integer cityId = findCityRecursive(countryArea, city.trim());
                        if (cityId != null) return cityId;
                    }
                }
            }
        } catch (Exception ex) {
            log.warn("Ошибка запроса HH API для страны '{}', города '{}': {}", country, city, ex.getMessage());
        }


        log.warn("Не найден ни город, ни страна для '{}', '{}'", country, city);
        return null;
    }

    private Integer findCityRecursive(HhAreaResponse area, String city) {
        if (area.getName() != null && area.getName().equalsIgnoreCase(city)) {
            return area.getId();
        }
        if (area.getAreas() != null) {
            for (HhAreaResponse sub : area.getAreas()) {
                Integer found = findCityRecursive(sub, city);
                if (found != null) return found;
            }
        }
        return null;
    }

}

