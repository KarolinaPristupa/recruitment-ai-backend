package com.example.server.dto.response;

import lombok.Data;

@Data
public class HhAuthTokenResponse {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private Integer expires_in;
}
