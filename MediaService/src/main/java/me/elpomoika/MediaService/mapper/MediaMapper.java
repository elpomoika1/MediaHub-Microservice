package me.elpomoika.MediaService.mapper;

import me.elpomoika.MediaService.dto.media.MediaPreviewResponse;
import me.elpomoika.MediaService.domain.entity.Media;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.Mapping;

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