package me.elpomoika.MediaService.application.dto.media;

public record EpisodeRequest(
    int episodeNumber,
    String providerName,
    int quality,
    String url) { }