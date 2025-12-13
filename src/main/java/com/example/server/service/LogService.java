package com.example.server.service;

import com.example.server.model.enums.ActionType;
import com.example.server.model.Log;
import com.example.server.model.User;
import com.example.server.repository.LogRepository;
import com.example.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;
    public void log(User user, ActionType action, String details) {
        Log log = new Log();
        log.setUser(user);
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        logRepository.save(log);
    }

    public List<Log> getLogsByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь с email " + email + " не найден"));
        return logRepository.findByUserId(user.getId());
    }
}
