package me.elpomoika.AuthenticationService.dto;

public record ApiError(
        String code,
        String message) { }
