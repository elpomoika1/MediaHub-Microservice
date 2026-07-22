package me.elpomoika.AuthenticationService.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.domain.entity.OutboxEvent;
import me.elpomoika.AuthenticationService.domain.entity.RefreshToken;
import me.elpomoika.AuthenticationService.domain.entity.User;
import me.elpomoika.AuthenticationService.domain.enums.Role;
import me.elpomoika.AuthenticationService.dto.RefreshRequest;
import me.elpomoika.AuthenticationService.dto.UserDto;
import me.elpomoika.AuthenticationService.dto.auth.*;
import me.elpomoika.AuthenticationService.producer.event.UserRegisteredEvent;
import me.elpomoika.AuthenticationService.domain.enums.OutboxStatus;
import me.elpomoika.AuthenticationService.producer.outbox.repository.OutboxEventRepository;
import me.elpomoika.AuthenticationService.repository.UserRepository;
import me.elpomoika.AuthenticationService.security.jwt.JwtService;
import me.elpomoika.AuthenticationService.security.jwt.RefreshTokenService;
import me.elpomoika.AuthenticationService.util.JsonParser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final OutboxEventRepository outboxRepository;
    private final JsonParser jsonParser;

    public UserDto changeEmail(ChangeEmailRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow();

        user.setEmail(request.email());

        userRepository.save(user);

        return UserDto.builder()
                .email(user.getEmail())
                .login(user.getLogin())
                .build();
    }

    public UserDto changePassword(ChangePasswordRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow();

        user.setPassword(request.password());

        userRepository.save(user);

        return UserDto.builder()
                .email(user.getEmail())
                .login(user.getLogin())
                .build();
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

    @Transactional
    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setRoles(Collections.singleton(Role.MEMBER));
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);

        enqueueEvent(saved, Instant.now());

        return UserDto.builder()
                .email(saved.getEmail())
                .login(saved.getLogin())
                .build();
    }

    public AuthResponse refresh(RefreshRequest request) {
        RefreshToken stored = refreshTokenService.validate(request.getRefreshToken());

        User user = stored.getUser();
        String newAccessToken = jwtService.generateToken(user);

        return new AuthResponse(newAccessToken, stored.getToken());
    }

    private void enqueueEvent(User user, Instant now) {
        UserRegisteredEvent payload = UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .build();

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateId(user.getId().toString())
                .eventType("user.registered")
                .payload(jsonParser.toJson(payload))
                .retryCount(0)
                .availableAt(now)
                .updatedAt(now)
                .createdAt(now)
                .status(OutboxStatus.NEW)
                .build();

        outboxRepository.save(outboxEvent);
    }
}
