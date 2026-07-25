package me.elpomoika.MediaService.repository;

import me.elpomoika.MediaService.domain.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByMedia_IdAndUserId(Long mediaId, UUID userId);

    @Query("SELECT AVG(r.value) FROM Rating r WHERE r.media.id = :mediaId")
    Double findAverageRatingByMediaId(@Param("mediaId") Long mediaId);

    @Query("SELECT r.media.id AS mediaId, AVG(r.value) AS avg FROM Rating r WHERE r.media.id IN :mediaIds GROUP BY r.media.id")
    List<MediaAverageRatingProjection> findAverageRatingsByMediaIds(@Param("mediaIds") List<Long> mediaIds);

    interface MediaAverageRatingProjection {
        Long getMediaId();
        Double getAvg();
    }
}
