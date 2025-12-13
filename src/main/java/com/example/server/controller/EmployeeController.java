package com.example.server.controller;

import com.example.server.dto.request.EnterpriseWithAdminRegistrationDTO;
import com.example.server.dto.request.UserRegistrationDTO;
import com.example.server.dto.response.UserAccountResponseDTO;
import com.example.server.model.Log;
import com.example.server.model.User;
import com.example.server.service.LogService;
import com.example.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enterprise")
@RequiredArgsConstructor
public class EmployeeController {

    private final UserService userService;
    private final LogService logService;

    @GetMapping
    public ResponseEntity<List<UserAccountResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{enterpriseId}/users")
    public ResponseEntity<List<UserAccountResponseDTO>> getUsersByEnterprise(@PathVariable Long enterpriseId) {
        return ResponseEntity.ok(userService.getUsersByEnterprise(enterpriseId));
    }

    @GetMapping("/users/{email}/logs")
    public ResponseEntity<List<Log>> getUserLogsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(logService.getLogsByUserEmail(email));
    }

    @PostMapping("/users")
    public ResponseEntity<UserAccountResponseDTO> createUser(
            @RequestBody UserRegistrationDTO dto,
            Authentication auth
    ) {
        if (auth == null) return ResponseEntity.status(403).build();

        User currentUser = (User) auth.getPrincipal();
        UserAccountResponseDTO created;

        if ("HR".equalsIgnoreCase(dto.getRole())) {
            dto.setEnterpriseId(currentUser.getEnterprise().getId());
            User savedUser = userService.registerHR(dto, currentUser);
            created = userService.toDto(savedUser);
        } else if ("ENT_ADMIN".equalsIgnoreCase(dto.getRole())) {
            User savedAdmin = userService.createEnterpriseAdmin(
                    new EnterpriseWithAdminRegistrationDTO(dto),
                    currentUser.getEnterprise()
            );
            created = userService.toDto(savedAdmin);
        } else {
            created = userService.createEmployeeForEnterprise(dto, currentUser);
        }

        return ResponseEntity.ok(created);
    }

    @PutMapping("/users/{email}")
    public ResponseEntity<UserAccountResponseDTO> updateUser(
            @PathVariable String email,
            @RequestBody UserRegistrationDTO dto,
            Authentication auth
    ) {
        if (auth == null) return ResponseEntity.status(403).build();

        UserAccountResponseDTO updated = userService.updateEmployeeForEnterpriseByEmail(email, dto, (User) auth.getPrincipal());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/users/{email}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String email,
            Authentication auth
    ) {
        if (auth == null) return ResponseEntity.status(403).build();

        userService.deleteEmployeeForEnterpriseByEmail(email, (User) auth.getPrincipal());
        return ResponseEntity.noContent().build();
    }


}
