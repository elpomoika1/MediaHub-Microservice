package me.elpomoika.AuthenticationService.presentation.controller;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.application.dto.RefreshRequest;
import me.elpomoika.AuthenticationService.application.dto.UserDto;
import me.elpomoika.AuthenticationService.application.dto.auth.*;
import me.elpomoika.AuthenticationService.application.usecases.*;
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
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ChangeEmailUseCase changeEmailUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterRequest request) {
        return registerUseCase.execute(request);
    }

    @PostMapping("/changepassword")
    public UserDto changePassword(@RequestBody ChangePasswordRequest request,
                                  Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return changePasswordUseCase.execute(request, userId);
    }

    @PostMapping("/changeemail")
    public UserDto changeEmail(@RequestBody ChangeEmailRequest request,
                               Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return changeEmailUseCase.execute(request, userId);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return loginUseCase.execute(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {
        return refreshTokenUseCase.execute(request);
    }
}