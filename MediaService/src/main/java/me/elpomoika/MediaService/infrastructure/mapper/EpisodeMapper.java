package me.elpomoika.MediaService.infrastructure.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.elpomoika.MediaService.application.dto.media.EpisodeResponse;
import me.elpomoika.MediaService.domain.entity.Episode;
import me.elpomoika.MediaService.domain.entity.PlayerSource;

@Component
@RequiredArgsConstructor
public class EpisodeMapper {
    private final PlayerSourceMapper playerMapper;

    public EpisodeResponse toResponse(Episode episode) {
        List<PlayerSource> players = episode.getSources();
        return new EpisodeResponse(
            episode.getNumber(),
            playerMapper.toResponse(players)
        );
    }

    public List<EpisodeResponse> toResponse(List<Episode> episodes) {
        if (episodes == null || episodes.isEmpty()) {
            return List.of();
        }

        return episodes.stream()
                .map(this::toResponse)
                .toList();
    }
}
