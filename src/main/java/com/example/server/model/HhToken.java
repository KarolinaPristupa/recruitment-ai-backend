package com.example.server.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "hh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HhToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_id", nullable = false, unique = true)
    private User hr;

    @Column(name = "access_token", nullable = false, length = 500)
    private String accessToken;

    @Column(name = "refresh_token", length = 500)
    private String refreshToken;

    @Column(name = "expires_at")
    private Instant expiresAt;
}
