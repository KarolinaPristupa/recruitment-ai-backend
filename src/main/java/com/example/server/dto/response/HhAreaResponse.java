package com.example.server.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class HhAreaResponse {
    private String id;
    private String name;
    private List<HhAreaResponse> areas;

    public Integer find(String city, String country) {
        if (name.equalsIgnoreCase(country)) {
            return findCity(city);
        }

        if (areas != null) {
            for (HhAreaResponse a : areas) {
                Integer id = a.find(city, country);
                if (id != null) return id;
            }
        }
        return null;
    }

    private Integer findCity(String city) {
        if (name.equalsIgnoreCase(city)) {
            return Integer.valueOf(id);
        }

        if (areas != null) {
            for (HhAreaResponse a : areas) {
                Integer found = a.findCity(city);
                if (found != null) return found;
            }
        }

        return null;
    }
}

