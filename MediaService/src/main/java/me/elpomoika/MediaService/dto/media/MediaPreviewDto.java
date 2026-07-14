package me.elpomoika.MediaService.dto.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MediaPreviewDto {
    private String name;
    private String title;
    private String imageUrl;
    private double rating;
    private double votes;
}
