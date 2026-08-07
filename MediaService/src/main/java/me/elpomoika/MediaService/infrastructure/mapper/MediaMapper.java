package me.elpomoika.MediaService.infrastructure.mapper;

import me.elpomoika.MediaService.application.dto.media.MediaPreviewResponse;
import me.elpomoika.MediaService.domain.entity.Media;
import org.springframework.stereotype.Service;

@Service
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