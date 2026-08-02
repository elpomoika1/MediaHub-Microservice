package me.elpomoika.MediaService.repository;

import me.elpomoika.MediaService.domain.entity.Media;

import java.util.List;

public interface MediaRepositorySearch {
    List<Media> searchMedia(String title);
}
