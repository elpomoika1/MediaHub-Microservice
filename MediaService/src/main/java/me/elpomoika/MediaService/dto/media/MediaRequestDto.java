package me.elpomoika.MediaService.dto.media;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.elpomoika.MediaService.domain.enums.Genre;
import me.elpomoika.MediaService.domain.enums.MediaType;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class MediaRequestDto {
    private final String title;
    private final MediaType type;
    private final List<Genre> genres;
}
