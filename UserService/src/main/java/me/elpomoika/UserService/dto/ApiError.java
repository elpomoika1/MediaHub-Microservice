package me.elpomoika.UserService.dto;

public record ApiError(
        String code,
        String message) { }
