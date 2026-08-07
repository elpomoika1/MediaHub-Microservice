package me.elpomoika.MediaService.infrastructure.jpa;

import me.elpomoika.MediaService.domain.entity.Media;

import java.util.List;

public interface MediaRepositorySearch {
    List<Media> searchMedia(String title);
}
