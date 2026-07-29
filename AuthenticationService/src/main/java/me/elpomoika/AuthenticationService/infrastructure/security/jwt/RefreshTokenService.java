package me.elpomoika.AuthenticationService.infrastructure.security.jwt;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.domain.entities.RefreshToken;
import me.elpomoika.AuthenticationService.domain.entities.User;
import me.elpomoika.AuthenticationService.infrastructure.persistence.jpa.JpaRefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    public RefreshToken create(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(
                Instant.now().plus(7, ChronoUnit.DAYS)
        );
        token.setRevoked(false);

        return jpaRefreshTokenRepository.save(token);
    }

    public RefreshToken validate(String token) {
        return jpaRefreshTokenRepository.findByToken(token)
                .filter(t -> !t.isRevoked())
                .filter(t -> t.getExpiresAt().isAfter(Instant.now()))
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }
}
