package me.elpomoika.MediaService.infrastructure.service;

import lombok.RequiredArgsConstructor;
import me.elpomoika.MediaService.domain.entity.Episode;
import me.elpomoika.MediaService.domain.entity.Media;
import me.elpomoika.MediaService.domain.entity.PlayerSource;
import me.elpomoika.MediaService.domain.entity.Rating;
import me.elpomoika.MediaService.domain.enums.Genre;
import me.elpomoika.MediaService.domain.enums.MediaType;
import me.elpomoika.MediaService.application.dto.PlayerSourceResponse;
import me.elpomoika.MediaService.application.dto.comment.CommentResponse;
import me.elpomoika.MediaService.application.dto.media.EpisodeRequest;
import me.elpomoika.MediaService.application.dto.media.EpisodeResponse;
import me.elpomoika.MediaService.application.dto.media.GenreRequest;
import me.elpomoika.MediaService.application.dto.media.MediaPreviewResponse;
import me.elpomoika.MediaService.application.dto.media.MediaRequest;
import me.elpomoika.MediaService.application.dto.media.MediaResponse;
import me.elpomoika.MediaService.application.dto.media.RatingRequest;
import me.elpomoika.MediaService.infrastructure.grpc.AddMovieRequest;
import me.elpomoika.MediaService.infrastructure.grpc.RecommendationServiceGrpc;
import me.elpomoika.MediaService.infrastructure.mapper.CommentMapper;
import me.elpomoika.MediaService.infrastructure.mapper.EpisodeMapper;
import me.elpomoika.MediaService.infrastructure.mapper.MediaMapper;
import me.elpomoika.MediaService.infrastructure.mapper.PlayerSourceMapper;
import me.elpomoika.MediaService.infrastructure.jpa.GenreRepository;
import me.elpomoika.MediaService.infrastructure.jpa.MediaRepository;
import me.elpomoika.MediaService.infrastructure.jpa.RatingRepository;
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
    private final GenreRepository genreRepository;
    private final MediaMapper mediaMapper;
    private final CommentMapper commentMapper;
    private final EpisodeMapper episodeMapper;

    private final RecommendationServiceGrpc.RecommendationServiceBlockingStub stub;

    public void saveMovie(MultipartFile file, MediaRequest request) throws IOException {
        // todo create grpc request to save
        String title = request.title();
        Media media = Media.builder()
                .title(title)
                .episodesCount(request.episodesCount())
                .type(request.type())
                .genres(getGenres(request.genres()))
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

        AddMovieRequest grpcRequest = AddMovieRequest.newBuilder()
                        .addGenreIds(media.getId())
                .addAllGenreIds(media.getGenres().stream()
                        .map(Genre::getId)
                        .collect(Collectors.toList()))
                .build();

        //todo handle succecss
        stub.addMovie(grpcRequest);
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

    public List<MediaPreviewResponse> getMedias() {
        return toPreviewList(mediaRepository.findAll());
    }

    public List<MediaPreviewResponse> getMediasByTypeAndGenres(MediaType type, List<Genre> genres) {
        return toPreviewList(mediaRepository.findDistinctByTypeAndGenresIn(type, genres));
    }

    public Media getMediaEntity(String slug) {
        return mediaRepository.findByName(slug);
    }

    public MediaResponse getMediaDetails(String slug, UUID userId) {
        Media media = getMediaEntity(slug);

        // todo handle userId == null
        // todo grpc client
        Rating userRating = ratingRepository.findByMediaIdAndUserId(media.getId(), userId).get();
        Double avgRating = ratingRepository.findAverageRatingByMediaId(media.getId());
        List<CommentResponse> comments = commentMapper.toResponse(media.getComments());
        List<EpisodeResponse> episodes = episodeMapper.toResponse(media.getEpisodes());

        return new MediaResponse(
            media.getName(),
            media.getTitle(),
            media.getEpisodesCount(),
            userRating.getValue(),
            media.getType(),
            media.getImageUrl(),
            avgRating,
            comments,
            episodes,
            List.of()
        );
    }

    public List<MediaPreviewResponse> getMediasByType(MediaType type) {
        return toPreviewList(mediaRepository.findByType(type));
    }

    private List<MediaPreviewResponse> toPreviewList(List<Media> mediaList) {
        if (mediaList.isEmpty()) {
            return List.of();
        }

        List<Long> ids = mediaList.stream().map(Media::getId).toList();
        Map<Long, Double> avgRatings = getAvgRating(ids);

        return mediaList.stream()
                .map(media -> mediaMapper.toPreview(
                        media,
                        avgRatings.getOrDefault(media.getId(), 0.0)))
                .toList();
    }

    public List<Genre> getGenres(List<GenreRequest> requests) {
        return requests.stream()
            .map(request ->
                    genreRepository.findByNameIgnoreCase(request.name())
                        .orElseThrow(
                            () -> new RuntimeException(
                                "Genre not found"
                            )
                        )).toList();
    }

    private Map<Long, Double> getAvgRating(List<Long> ids) {
        return ratingRepository.findAverageRatingsByMediaIds(ids).stream()
                .collect(Collectors.toMap(
                        RatingRepository.MediaAverageRatingProjection::getMediaId,
                        RatingRepository.MediaAverageRatingProjection::getAvg
                ));
    }
}
