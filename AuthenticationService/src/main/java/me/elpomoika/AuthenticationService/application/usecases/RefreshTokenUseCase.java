package me.elpomoika.AuthenticationService.application.usecases;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.application.dto.RefreshRequest;
import me.elpomoika.AuthenticationService.application.dto.auth.AuthResponse;
import me.elpomoika.AuthenticationService.domain.entities.RefreshToken;
import me.elpomoika.AuthenticationService.domain.entities.User;
import me.elpomoika.AuthenticationService.infrastructure.security.jwt.JwtService;
import me.elpomoika.AuthenticationService.infrastructure.security.jwt.RefreshTokenService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthResponse execute(RefreshRequest request) {
        RefreshToken stored = refreshTokenService.validate(request.getRefreshToken());

        User user = stored.getUser();
        String newAccessToken = jwtService.generateToken(user);

        return new AuthResponse(newAccessToken, stored.getToken());
    }
}
