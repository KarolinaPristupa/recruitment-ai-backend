package com.example.server.service;

import com.example.server.model.enums.ActionType;
import com.example.server.model.Log;
import com.example.server.model.User;
import com.example.server.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public void log(User user, ActionType action, String details) {
        Log log = new Log();
        log.setUser(user);
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        logRepository.save(log);
    }
}
