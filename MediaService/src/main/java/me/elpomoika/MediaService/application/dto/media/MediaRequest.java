package me.elpomoika.MediaService.application.dto.media;

import java.util.List;

import me.elpomoika.MediaService.domain.enums.MediaType;

public record MediaRequest(
    String title,
    int episodesCount,
    EpisodeRequest episodeRequest,
    MediaType type,
    List<GenreRequest> genres) {
}
