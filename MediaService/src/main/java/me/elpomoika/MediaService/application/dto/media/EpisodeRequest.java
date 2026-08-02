package me.elpomoika.MediaService.dto.media;

public record EpisodeRequest(
    int episodeNumber,
    String providerName,
    int quality,
    String url) { }