package me.elpomoika.AuthenticationService.application.usecases;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.application.dto.auth.AuthResponse;
import me.elpomoika.AuthenticationService.application.dto.auth.LoginRequest;
import me.elpomoika.AuthenticationService.domain.entities.RefreshToken;
import me.elpomoika.AuthenticationService.domain.entities.User;
import me.elpomoika.AuthenticationService.infrastructure.persistence.repository.UserRepositoryImpl;
import me.elpomoika.AuthenticationService.infrastructure.security.jwt.JwtService;
import me.elpomoika.AuthenticationService.infrastructure.security.jwt.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepositoryImpl userRepository;

    public AuthResponse execute(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                ));

        User user = userRepository.findByEmail(request.email());

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }
}
