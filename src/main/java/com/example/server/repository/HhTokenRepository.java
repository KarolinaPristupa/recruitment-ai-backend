package com.example.server.repository;

import com.example.server.model.HhToken;
import com.example.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HhTokenRepository extends JpaRepository<HhToken, Long> {
    Optional<HhToken> findByHr(User hr);
    Optional<HhToken> findByHrId(Long hrId);
}

