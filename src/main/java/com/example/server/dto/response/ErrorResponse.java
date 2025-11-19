package com.example.server.dto.response;

public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String code,
        String message,
        String path
) {}