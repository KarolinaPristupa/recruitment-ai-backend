package com.example.server.service.strategy;


import com.example.server.model.Log;
import com.example.server.model.enums.ActionType;
import com.example.server.dto.response.LogStatisticsResponseDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserActivityStatisticsStrategy implements LogStatisticsStrategy {

    @Override
    public String getType() {
        return "user_activity";
    }

    @Override
    public LogStatisticsResponseDTO calculate(List<Log> logs) {
        Map<String, Map<String, Long>> result = new HashMap<>();

        logs.stream()
                .filter(log -> log.getAction() == ActionType.UPDATE_HR
                        || log.getAction() == ActionType.UPDATE_EMPLOYEE
                        || log.getAction() == ActionType.UPDATE_ENTERPRISE
                        || log.getAction() == ActionType.VIEW_ENT_USERS)
                .forEach(log -> {
                    String userName = log.getUser().getEmail();
                    String action = log.getAction().name();
                    result.putIfAbsent(userName, new HashMap<>());
                    Map<String, Long> userMap = result.get(userName);
                    userMap.put(action, userMap.getOrDefault(action, 0L) + 1);
                });

        return LogStatisticsResponseDTO.builder()
                .type(getType())
                .data(result.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .build();
    }
}
