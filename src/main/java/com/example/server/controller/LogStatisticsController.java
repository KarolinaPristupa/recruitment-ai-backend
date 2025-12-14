package com.example.server.controller;

import com.example.server.dto.response.LogStatisticsResponseDTO;
import com.example.server.service.LogStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs/statistics")
@RequiredArgsConstructor
public class LogStatisticsController {

    private final LogStatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<List<LogStatisticsResponseDTO>> getAll() {
        return ResponseEntity.ok(statisticsService.getAllStatistics());
    }

    @GetMapping("/{type}")
    public ResponseEntity<LogStatisticsResponseDTO> getByType(@PathVariable String type) {
        return ResponseEntity.ok(statisticsService.getStatisticsByType(type));
    }
}
