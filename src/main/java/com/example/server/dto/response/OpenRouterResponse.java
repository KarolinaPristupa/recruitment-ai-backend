package com.example.server.dto.response;

import lombok.Data;

@Data
public class OpenRouterResponse {
    private Choice[] choices;

    @Data
    public static class Choice {
        private Message message;

        @Data
        public static class Message {
            private String content;
        }
    }
}
