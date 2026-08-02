package me.elpomoika.MediaService.repository;

import me.elpomoika.MediaService.domain.entity.Media;
import me.elpomoika.MediaService.domain.enums.Genre;
import me.elpomoika.MediaService.domain.enums.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long>, MediaRepositorySearch {
    Media findByName(String name);
    List<Media> findByType(MediaType mediaType);
    List<Media> findDistinctByTypeAndGenresIn(MediaType type, List<Genre> genres);
}
