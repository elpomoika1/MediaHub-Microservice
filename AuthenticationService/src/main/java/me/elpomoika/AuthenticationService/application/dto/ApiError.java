package me.elpomoika.AuthenticationService.application.dto;

public record ApiError(
        String code,
        String message) { }
