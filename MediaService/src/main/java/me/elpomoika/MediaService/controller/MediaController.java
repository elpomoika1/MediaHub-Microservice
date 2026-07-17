package me.elpomoika.MediaService.controller;

import lombok.RequiredArgsConstructor;
import me.elpomoika.MediaService.domain.entity.Media;
import me.elpomoika.MediaService.domain.enums.Genre;
import me.elpomoika.MediaService.domain.enums.MediaType;
import me.elpomoika.MediaService.dto.comment.CommentVoteRequest;
import me.elpomoika.MediaService.dto.media.CommentRequest;
import me.elpomoika.MediaService.dto.media.MediaPreviewDto;
import me.elpomoika.MediaService.dto.media.MediaRequestDto;
import me.elpomoika.MediaService.dto.media.RatingRequest;
import me.elpomoika.MediaService.mapper.MediaMapper;
import me.elpomoika.MediaService.service.CommentService;
import me.elpomoika.MediaService.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;
    private final CommentService commentService;
    private final MediaMapper mediaMapper;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") MediaRequestDto request) throws IOException {
        mediaService.saveMovie(file, request);
        return ResponseEntity.ok("Uploaded");
    }

    @GetMapping("/find")
    public ResponseEntity<MediaPreviewDto> getMedia(@RequestParam String name) {
        return ResponseEntity.ok(
                mediaMapper.toDto(mediaService.getMediaBySlug(name))
        );
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("all is good boy");
    }

    @GetMapping("/list")
    public ResponseEntity<List<MediaPreviewDto>> getMedias() {
        return ResponseEntity.ok(mediaService.getMedias().stream()
                .map(mediaMapper::toDto)
                .toList()
        );
    }

    @GetMapping("/list/{type}")
    public ResponseEntity<List<MediaPreviewDto>> getMediasByType(
            @PathVariable MediaType type,
            @RequestParam(required = false) List<Genre> genres) {
        List<Media> medias;

        if (genres == null || genres.isEmpty()) {
            medias = mediaService.getMediasByType(type);
        } else {
            medias = mediaService.getMediasByTypeAndGenres(type, genres);
        }

        return ResponseEntity.ok(
                medias.stream()
                        .map(mediaMapper::toDto)
                        .toList());
    }

    @GetMapping("/random")
    public ResponseEntity<MediaPreviewDto> getRandomMedia() {
        return ResponseEntity.ok(
                mediaMapper.toDto(mediaService.getRandomMovie())
        );
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<MediaPreviewDto>> searchResults(@PathVariable String title) {
        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);

        return ResponseEntity.ok(mediaService.searchMedia(decodedTitle).stream()
                .map(mediaMapper::toDto)
                .collect(Collectors.toList()));
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/{name}/ratings")
    public ResponseEntity<?> rateMedia(@PathVariable String name, @RequestBody RatingRequest request) {
        mediaService.rateMedia(name, request);
        return ResponseEntity.ok("rated");
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/{name}/comment")
    public ResponseEntity<?> commentMedia(@PathVariable String name, @RequestBody CommentRequest request, Authentication authentication) {
        UUID authorId = UUID.fromString(authentication.getName());
        commentService.leaveComment(authorId, name, request);

        return ResponseEntity.ok("commented");
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PatchMapping("/{name}/comment")
    public ResponseEntity<?> editComment(@PathVariable String name, @RequestBody CommentRequest request, Authentication authentication) {
        UUID authorId = UUID.fromString(authentication.getName());
        commentService.leaveComment(authorId, name, request);

        return ResponseEntity.ok("commented");
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PutMapping("/comments/{commentId}/vote")
    public ResponseEntity<?> vote(@PathVariable Long commentId, @RequestBody CommentVoteRequest request, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        commentService.rate(userId, commentId, request.type());

        return ResponseEntity.ok().build();
    }
}