package me.elpomoika.MediaService.service;

import lombok.RequiredArgsConstructor;
import me.elpomoika.MediaService.domain.entity.Media;
import me.elpomoika.MediaService.domain.entity.Rating;
import me.elpomoika.MediaService.domain.enums.Genre;
import me.elpomoika.MediaService.domain.enums.MediaType;
import me.elpomoika.MediaService.dto.media.MediaRequestDto;
import me.elpomoika.MediaService.repository.MediaRepository;
import me.elpomoika.MediaService.util.SlugGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;
    private final S3FileStorageService s3StorageService;

    public void saveMovie(MultipartFile file, MediaRequestDto request) throws IOException {
        String title = request.getTitle();
        Media media = Media.builder()
                .title(title)
                .type(request.getType())
                .genres(request.getGenres())
                .build();

        media = mediaRepository.save(media);

        final String slug = SlugGenerator.generateSlug(title, media.getId());
        final String imageUrl = s3StorageService.uploadFile(file, slug);

        media.setName(slug);
        media.setImageUrl(imageUrl);

        mediaRepository.save(media);
    }

    public void rateMedia(UUID userId, String name, double inputRating) {
        if (inputRating > 10 || inputRating <= 0) return;

        Media media = mediaRepository.findByName(name);
        if (media == null) return;

        Rating rating = Rating.builder()
                .media(media)
                .value(inputRating)
                .build();

        media.getRating().add(rating);
        mediaRepository.save(media);
    }

    public Media getRandomMovie() {
        final Random random = new Random();
        List<Media> media = getMedias();

        return media.get(random.nextInt(media.size()));
    }

    public List<Media> searchMedia(String title) {
        return mediaRepository.searchMedia(title);
    }

    public Media getMediaBySlug(String slug) {
        return mediaRepository.findByName(slug);
    }

    public List<Media> getMedias() {
        return mediaRepository.findAll();
    }

    public List<Media> getMediasByTypeAndGenres(MediaType mediaType, List<Genre> genres) {
        return mediaRepository.findDistinctByTypeAndGenresIn(mediaType, genres);
    }

    public List<Media> getMediasByType(MediaType mediaType) {
        return mediaRepository.findByType(mediaType);
    }
}