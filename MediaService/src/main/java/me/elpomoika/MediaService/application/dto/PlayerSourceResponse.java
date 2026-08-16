package me.elpomoika.MediaService.application.dto;

public record PlayerSourceResponse(
    Long id,
    String providerName,
    String quality,
    String url
) {
}
