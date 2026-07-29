package me.elpomoika.AuthenticationService.infrastructure.persistence.jpa;

import me.elpomoika.AuthenticationService.domain.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
