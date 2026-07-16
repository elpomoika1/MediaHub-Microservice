package me.elpomoika.AuthenticationService.security.service;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.domain.entity.RefreshToken;
import me.elpomoika.AuthenticationService.domain.entity.User;
import me.elpomoika.AuthenticationService.dto.auth.*;
import me.elpomoika.AuthenticationService.dto.RefreshRequest;
import me.elpomoika.AuthenticationService.dto.UserDto;
import me.elpomoika.AuthenticationService.repository.UserRepository;
import me.elpomoika.AuthenticationService.security.jwt.JwtService;
import me.elpomoika.AuthenticationService.security.jwt.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public UserDto changeEmail(ChangeEmailRequest request, UUID userId) {
        User user = userRepository.findByUuid(userId)
                .orElseThrow();

        user.setEmail(request.email());

        return new UserDto(user.getEmail());
    }

    public UserDto changePassword(ChangePasswordRequest request, UUID userId) {
        User user = userRepository.findByUuid(userId)
                .orElseThrow();

        user.setPassword(request.password());

        return new UserDto(user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User saved = userRepository.save(user);

        return new UserDto(saved.getEmail());
    }

    public AuthResponse refresh(RefreshRequest request) {
        RefreshToken stored = refreshTokenService.validate(request.getRefreshToken());

        User user = stored.getUser();
        String newAccessToken = jwtService.generateToken(user);

        return new AuthResponse(newAccessToken, stored.getToken());
    }
}
