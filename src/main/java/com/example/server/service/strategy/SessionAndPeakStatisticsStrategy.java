package com.example.server.service.strategy;

import com.example.server.model.Log;
import com.example.server.dto.response.LogStatisticsResponseDTO;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SessionAndPeakStatisticsStrategy implements LogStatisticsStrategy {

    @Override
    public String getType() {
        return "sessions";
    }

    @Override
    public LogStatisticsResponseDTO calculate(List<Log> logs) {
        Map<DayOfWeek, Map<Integer, Long>> heatmap = new HashMap<>();

        logs.forEach(log -> {
            DayOfWeek day = log.getTimestamp().getDayOfWeek();
            int hour = log.getTimestamp().getHour();
            heatmap.putIfAbsent(day, new HashMap<>());
            Map<Integer, Long> hourMap = heatmap.get(day);
            hourMap.put(hour, hourMap.getOrDefault(hour, 0L) + 1);
        });

        return LogStatisticsResponseDTO.builder()
                .type(getType())
                .data(Map.of("heatmap", heatmap))
                .build();
    }
}
