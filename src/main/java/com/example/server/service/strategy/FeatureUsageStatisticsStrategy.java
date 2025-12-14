package com.example.server.service.strategy;
import com.example.server.model.Log;
import com.example.server.dto.response.LogStatisticsResponseDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeatureUsageStatisticsStrategy implements LogStatisticsStrategy {

    @Override
    public String getType() {
        return "feature_usage";
    }

    @Override
    public LogStatisticsResponseDTO calculate(List<Log> logs) {
        Map<String, Long> usage = new HashMap<>();
        logs.forEach(log -> {
            String action = log.getAction().name();
            usage.put(action, usage.getOrDefault(action, 0L) + 1);
        });

        return LogStatisticsResponseDTO.builder()
                .type(getType())
                .data(Map.of("usage", usage))
                .build();
    }
}
