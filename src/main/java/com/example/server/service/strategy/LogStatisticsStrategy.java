package com.example.server.service.strategy;

import com.example.server.model.Log;
import com.example.server.dto.response.LogStatisticsResponseDTO;

import java.util.List;

public interface LogStatisticsStrategy {
    String getType();
    LogStatisticsResponseDTO calculate(List<Log> logs);
}
