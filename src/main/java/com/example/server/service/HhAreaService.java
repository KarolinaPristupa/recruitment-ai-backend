package com.example.server.service;

import com.example.server.client.HhClient;
import com.example.server.model.enums.HhRegion;
import com.example.server.model.enums.Platform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        Integer localId = HhRegion.getIdByCity(city);
        if (localId != null) {
            return localId;
        }

        try {
            Integer apiId = hhClient.findAreaId(country, city);
            if (apiId != null) {
                return apiId;
            }
        } catch (Exception ex) {
            log.warn("Ошибка запроса HH API для города '{}': {}", city, ex.getMessage());
        }

        Platform platform = Platform.fromCountry(country);

        if (platform != null) {
            return platform.getDefaultAreaId();
        }

        log.warn("Не найден ни город, ни страна для '{}', '{}'", country, city);
        return null;
    }

    public Platform getPlatformByCountry(String country) {
        return Platform.fromCountry(country);
    }
}
