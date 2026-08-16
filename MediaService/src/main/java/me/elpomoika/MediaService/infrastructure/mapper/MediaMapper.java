package me.elpomoika.MediaService.infrastructure.mapper;

import org.springframework.stereotype.Component;

import me.elpomoika.MediaService.application.dto.media.MediaPreviewResponse;
import me.elpomoika.MediaService.domain.entity.Media;

@Component
public class MediaMapper {
    public MediaPreviewResponse toPreview(Media media, double averageRating) {
        return MediaPreviewResponse.builder()
                .name(media.getName())
                .title(media.getTitle())
                .imageUrl(media.getImageUrl())
                .episodesCount(media.getEpisodesCount())
                .averageRating(averageRating)
                .type(media.getType())
                .build();
    }
}
