package com.example.server.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class HhProfessionalRolesResponse {
    private List<Category> categories;

    @Data
    public static class Category {
        private String id;
        private String name;
        private List<Role> roles;
    }

    @Data
    public static class Role {
        private String id;
        private String name;
    }
}
