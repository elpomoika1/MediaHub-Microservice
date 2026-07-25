package me.elpomoika.MediaService.dto.media;

import me.elpomoika.MediaService.domain.enums.Genre;
import me.elpomoika.MediaService.domain.enums.MediaType;

import java.util.List;

public record MediaRequest(
    String title,
    int episodesCount,
    EpisodeRequest episodeRequest,
    MediaType type,
    List<Genre> genres) {
}
