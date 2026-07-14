package me.elpomoika.AuthenticationService.controller;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.dto.auth.AuthResponse;
import me.elpomoika.AuthenticationService.dto.auth.LoginRequest;
import me.elpomoika.AuthenticationService.dto.RefreshRequest;
import me.elpomoika.AuthenticationService.dto.UserDto;
import me.elpomoika.AuthenticationService.dto.auth.RegisterRequest;
import me.elpomoika.AuthenticationService.security.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @PostMapping("/registration")
    public UserDto register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }
}