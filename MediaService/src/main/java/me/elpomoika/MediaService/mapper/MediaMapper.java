package me.elpomoika.MediaService.mapper;

import me.elpomoika.MediaService.dto.media.MediaPreviewDto;
import me.elpomoika.MediaService.domain.entity.Media;
import me.elpomoika.MediaService.util.ArithmeticMeanCalculator;
import org.springframework.stereotype.Service;

@Service
public class MediaMapper {
    public MediaPreviewDto toDto(Media media) {
        return MediaPreviewDto.builder()
                .name(media.getName())
                .title(media.getTitle())
                .imageUrl(media.getImageUrl())
                .rating(ArithmeticMeanCalculator.calculateAverage(media.getRating()))
                .votes(ArithmeticMeanCalculator.calculateAverage(media.getVotes()))
                .build();
    }
}