package com.example.server.repository;

import com.example.server.model.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
    Optional<Enterprise> findByName(String name);
    boolean existsByContactEmail(String contactEmail);
    boolean existsByContactPhone(String contactPhone);
}
