package me.elpomoika.MediaService.infrastructure.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.val;
import me.elpomoika.MediaService.application.dto.PlayerSourceResponse;
import me.elpomoika.MediaService.domain.entity.PlayerSource;

@Component
public class PlayerSourceMapper {
    public PlayerSourceResponse toResponse(PlayerSource player) {
        return new PlayerSourceResponse(
            player.getId(),
            player.getProviderName(),
            player.getQuality(),
            player.getUrl()
        );
    }

    public List<PlayerSourceResponse> toResponse(List<PlayerSource> players) {
        if (players == null || players.isEmpty()) {
            return List.of();
        }

        return players.stream()
            .map(this::toResponse)
            .toList();
    }
}
