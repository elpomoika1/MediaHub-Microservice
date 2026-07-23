package me.elpomoika.AuthenticationService.controller;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.dto.RefreshRequest;
import me.elpomoika.AuthenticationService.dto.UserDto;
import me.elpomoika.AuthenticationService.dto.auth.*;
import me.elpomoika.AuthenticationService.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/changepassword")
    public UserDto changePassword(@RequestBody ChangePasswordRequest request,
                                  Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return authService.changePassword(request, userId);
    }

    @PostMapping("/changeemail")
    public UserDto changeEmail(@RequestBody ChangeEmailRequest request,
                               Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return authService.changeEmail(request, userId);
    }

    @PostMapping("/register")
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