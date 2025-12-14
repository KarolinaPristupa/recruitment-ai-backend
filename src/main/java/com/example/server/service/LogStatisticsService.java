package com.example.server.service;

import com.example.server.model.Log;
import com.example.server.dto.response.LogStatisticsResponseDTO;
import com.example.server.model.User;
import com.example.server.model.enums.ActionType;
import com.example.server.repository.LogRepository;
import com.example.server.service.strategy.LogStatisticsStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogStatisticsService {

    private final LogRepository logRepository;
    private final List<LogStatisticsStrategy> strategies;
    private final LogService logService;
    private final UserService userService;
    public List<LogStatisticsResponseDTO> getAllStatistics() {
        List<Log> logs = logRepository.findAll();
        User currentUser = userService.getCurrentUser();
        logService.log(currentUser, ActionType.VIEW_ENT_STATISTICS,
                "Админ просмотрел всю статистику"
        );

        return strategies.stream()
                .map(strategy -> strategy.calculate(logs))
                .collect(Collectors.toList());
    }

    public LogStatisticsResponseDTO getStatisticsByType(String type) {
        List<Log> logs = logRepository.findAll();
        User currentUser = userService.getCurrentUser();
        logService.log(currentUser, ActionType.VIEW_ENT_STATISTICS,
                "Админ просмотрел статистику типа: " + type
        );

        return strategies.stream()
                .filter(s -> s.getType().equals(type))
                .findFirst()
                .map(s -> s.calculate(logs))
                .orElseThrow(() -> new RuntimeException("Статистика типа " + type + " не найдена"));
    }
}

