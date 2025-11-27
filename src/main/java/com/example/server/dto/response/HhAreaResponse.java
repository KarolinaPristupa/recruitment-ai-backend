package com.example.server.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class HhAreaResponse {
    private Integer id;
    private String name;
    private List<HhAreaResponse> areas;

    public Integer findExactCity(String city) {
        if (city == null) return null;

        if (name != null && name.equalsIgnoreCase(city)) {
            return id;
        }

        if (areas != null) {
            for (HhAreaResponse a : areas) {
                Integer result = a.findExactCity(city);
                if (result != null) return result;
            }
        }

        return null;
    }

}

