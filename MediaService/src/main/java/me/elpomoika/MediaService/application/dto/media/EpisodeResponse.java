package me.elpomoika.MediaService.application.dto.media;

import java.util.List;

import me.elpomoika.MediaService.application.dto.PlayerSourceResponse;

public record EpisodeResponse (
    int episodeNumber,
    List<PlayerSourceResponse> responses) { }
