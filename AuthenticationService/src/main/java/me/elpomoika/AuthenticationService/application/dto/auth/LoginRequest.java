package me.elpomoika.AuthenticationService.application.dto.auth;

public record LoginRequest(
    String email,
    String password) {
}
