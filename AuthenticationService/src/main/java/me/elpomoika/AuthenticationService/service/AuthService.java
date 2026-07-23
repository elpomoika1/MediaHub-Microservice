package me.elpomoika.AuthenticationService.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.domain.entity.RefreshToken;
import me.elpomoika.AuthenticationService.domain.entity.User;
import me.elpomoika.AuthenticationService.domain.enums.Role;
import me.elpomoika.AuthenticationService.dto.RefreshRequest;
import me.elpomoika.AuthenticationService.dto.UserDto;
import me.elpomoika.AuthenticationService.dto.auth.*;
import me.elpomoika.AuthenticationService.exception.UserExistsException;
import me.elpomoika.AuthenticationService.exception.UserNotFoundException;
import me.elpomoika.AuthenticationService.producer.event.UserChangeEmailEvent;
import me.elpomoika.AuthenticationService.producer.event.UserRegisteredEvent;
import me.elpomoika.AuthenticationService.producer.outbox.OutboxService;
import me.elpomoika.AuthenticationService.repository.UserRepository;
import me.elpomoika.AuthenticationService.security.jwt.JwtService;
import me.elpomoika.AuthenticationService.security.jwt.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final OutboxService outboxService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public UserDto changeEmail(ChangeEmailRequest request, UUID userId) {
        User user = getUserById(userId);

        user.setEmail(request.email());
        userRepository.save(user);

        UserChangeEmailEvent changeEmailEvent = UserChangeEmailEvent.builder()
                .userId(userId)
                .newEmail(request.email())
                .build();

        outboxService.publish(changeEmailEvent, "user.change.email", userId);

        return UserDto.builder()
                .email(user.getEmail())
                .login(user.getLogin())
                .build();
    }

    public UserDto changePassword(ChangePasswordRequest request, UUID userId) {
        User user = getUserById(userId);

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

        User user = getUserByEmail(request.getEmail());

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @Transactional
    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) throw new UserExistsException("User with this email already exists");

        User user = new User();
        user.setRoles(Collections.singleton(Role.MEMBER));
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);

        UserRegisteredEvent payload = UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .build();

        outboxService.publish(payload, "user.registered", user.getId());

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

    private User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found by userId"));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email"));
    }
}
