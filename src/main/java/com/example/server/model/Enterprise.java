package com.example.server.model;

import com.example.server.model.enums.EnterpriseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "enterprise")
public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String contactEmail;
    private String contactPhone;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EnterpriseStatus status = EnterpriseStatus.ACTIVE;

    private LocalDateTime updatedAt = LocalDateTime.now();
}


