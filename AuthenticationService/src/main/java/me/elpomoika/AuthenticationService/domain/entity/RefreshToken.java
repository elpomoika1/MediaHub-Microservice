package me.elpomoika.AuthenticationService.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private Instant expiresAt;
    private boolean revoked;

    @ManyToOne
    private User user;
}
