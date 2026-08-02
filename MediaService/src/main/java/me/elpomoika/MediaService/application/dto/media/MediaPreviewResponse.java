package me.elpomoika.MediaService.dto.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.elpomoika.MediaService.domain.enums.MediaType;

@Getter
@Builder
@AllArgsConstructor
public class MediaPreviewResponse {
    private String name;
    private int episodesCount;
    private Integer userRating;
    private String title;
    private MediaType type;
    private String imageUrl;
    private double averageRating;
}
