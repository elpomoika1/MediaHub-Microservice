package me.elpomoika.MediaService.service;

import lombok.RequiredArgsConstructor;
import me.elpomoika.MediaService.domain.entity.Episode;
import me.elpomoika.MediaService.domain.entity.Media;
import me.elpomoika.MediaService.domain.entity.PlayerSource;
import me.elpomoika.MediaService.domain.entity.Rating;
import me.elpomoika.MediaService.domain.enums.Genre;
import me.elpomoika.MediaService.domain.enums.MediaType;
import me.elpomoika.MediaService.dto.media.EpisodeRequest;
import me.elpomoika.MediaService.dto.media.MediaPreviewResponse;
import me.elpomoika.MediaService.dto.media.MediaRequest;
import me.elpomoika.MediaService.dto.media.RatingRequest;
import me.elpomoika.MediaService.mapper.MediaMapper;
import me.elpomoika.MediaService.repository.MediaRepository;
import me.elpomoika.MediaService.repository.RatingRepository;
import me.elpomoika.MediaService.util.SlugGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;
    private final RatingRepository ratingRepository;
    private final S3FileStorageService s3StorageService;
    private final MediaMapper mediaMapper;

    public void saveMovie(MultipartFile file, MediaRequest request) throws IOException {
        String title = request.title();
        Media media = Media.builder()
                .title(title)
                .episodesCount(request.episodesCount())
                .type(request.type())
                .genres(request.genres())
                .build();

        EpisodeRequest epReq = request.episodeRequest();
        Episode episode = Episode.builder()
                .number(epReq.episodeNumber())
                .media(media)
                .build();

        PlayerSource source = PlayerSource.builder()
                .providerName(epReq.providerName())
                .quality(String.valueOf(epReq.quality()))
                .url(epReq.url())
                .episode(episode)
                .build();

        episode.setSources(List.of(source));
        media.setEpisodes(List.of(episode));

        media = mediaRepository.save(media);

        final String slug = SlugGenerator.generateSlug(title, media.getId());
        final String imageUrl = s3StorageService.uploadFile(file, slug);

        media.setName(slug);
        media.setImageUrl(imageUrl);

        mediaRepository.save(media);
    }

    public void rateMedia(String name, UUID authorId, RatingRequest request) {
        if (request.rating() > 10 || request.rating() <= 0) return;

        Media media = mediaRepository.findByName(name);
        if (media == null) return;

        Rating rating = Rating.builder()
                .media(media)
                .value(request.rating())
                .userId(authorId)
                .build();

        media.getRatings().add(rating);
        mediaRepository.save(media);
    }

    public List<MediaPreviewResponse> searchMedia(String title) {
        return toPreviewList(mediaRepository.searchMedia(title));
    }

    public MediaPreviewResponse getMediaBySlug(String slug, UUID userId) {
        Media media = mediaRepository.findByName(slug);

        Integer userRating = userId == null
                ? null
                : ratingRepository.findByMediaIdAndUserId(media.getId(), userId)
                .map(Rating::getValue)
                .orElse(null);

        Double averageRating = ratingRepository.findAverageRatingByMediaId(media.getId());

        return MediaPreviewResponse.builder()
                .name(media.getName())
                .title(media.getTitle())
                .imageUrl(media.getImageUrl())
                .episodesCount(media.getEpisodesCount())
                .averageRating(averageRating)
                .userRating(userRating)
                .build();
    }

    public List<MediaPreviewResponse> getMedias() {
        return toPreviewList(mediaRepository.findAll());
    }

    public List<MediaPreviewResponse> getMediasByTypeAndGenres(MediaType type, List<Genre> genres) {
        return toPreviewList(mediaRepository.findDistinctByTypeAndGenresIn(type, genres));
    }

    public Media getMedia(String slug) {
        return mediaRepository.findByName(slug);
    }

    public List<MediaPreviewResponse> getMediasByType(MediaType type) {
        return toPreviewList(mediaRepository.findByType(type));
    }

    private List<MediaPreviewResponse> toPreviewList(List<Media> mediaList) {
        if (mediaList.isEmpty()) {
            return List.of();
        }

        List<Long> ids = mediaList.stream().map(Media::getId).toList();

        Map<Long, Double> avgRatings = ratingRepository.findAverageRatingsByMediaIds(ids).stream()
                .collect(Collectors.toMap(
                        RatingRepository.MediaAverageRatingProjection::getMediaId,
                        RatingRepository.MediaAverageRatingProjection::getAvg
                ));

        return mediaList.stream()
                .map(media -> mediaMapper.toPreview(
                        media,
                        avgRatings.getOrDefault(media.getId(), 0.0)))
                .toList();
    }
}