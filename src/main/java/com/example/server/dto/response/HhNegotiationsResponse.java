package com.example.server.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class HhNegotiationsResponse {
    private int found;
    private int page;
    private int pages;
    private int per_page;
    private List<HhResponse> items;

    @Data
    public static class HhResponse {
        private String id;
        private String created_at;
        private HhResume resume;
        private String message;

        @Data
        public static class HhResume {
            private String id;
            private String first_name;
            private String last_name;
            private HhFile file;

            @Data
            public static class HhFile {
                private String url;
            }
        }
    }
}
