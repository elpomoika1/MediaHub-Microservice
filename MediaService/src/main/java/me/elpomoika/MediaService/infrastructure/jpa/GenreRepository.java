package me.elpomoika.MediaService.infrastructure.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.elpomoika.MediaService.domain.enums.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByNameIgnoreCase(String name);
}
