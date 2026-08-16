package me.elpomoika.MediaService.presentation.controller;

import lombok.RequiredArgsConstructor;
import me.elpomoika.MediaService.domain.enums.Genre;
import me.elpomoika.MediaService.domain.enums.MediaType;
import me.elpomoika.MediaService.application.dto.comment.CommentVoteRequest;
import me.elpomoika.MediaService.application.dto.comment.CommentRequest;
import me.elpomoika.MediaService.application.dto.media.MediaPreviewResponse;
import me.elpomoika.MediaService.application.dto.media.MediaRequest;
import me.elpomoika.MediaService.application.dto.media.MediaResponse;
import me.elpomoika.MediaService.application.dto.media.RatingRequest;
import me.elpomoika.MediaService.infrastructure.service.CommentService;
import me.elpomoika.MediaService.infrastructure.service.MediaService;
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

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;
    private final CommentService commentService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") MediaRequest request) throws IOException {
        mediaService.saveMovie(file, request);
        return ResponseEntity.ok("Uploaded");
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("all is good boy");
    }

    @GetMapping("/list")
    public ResponseEntity<List<MediaPreviewResponse>> getMedias() {
        return ResponseEntity.ok(mediaService.getMedias());
    }

    @GetMapping("/list/{type}")
    public ResponseEntity<List<MediaPreviewResponse>> getMediasByType(
            @PathVariable MediaType type,
            @RequestParam(required = false) List<Genre> genres) {

        List<MediaPreviewResponse> medias = (genres == null || genres.isEmpty())
                ? mediaService.getMediasByType(type)
                : mediaService.getMediasByTypeAndGenres(type, genres);

        return ResponseEntity.ok(medias);
    }

    @GetMapping("/media/{slug}")
    public ResponseEntity<MediaResponse> getMedia(@PathVariable String slug, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(mediaService.getMediaDetails(slug, userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MediaPreviewResponse>> searchResults(@RequestParam String title) {
        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
        return ResponseEntity.ok(mediaService.searchMedia(decodedTitle));
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/{name}/ratings")
    public ResponseEntity<?> rateMedia(@PathVariable String name, @RequestBody RatingRequest request, Authentication authentication) {
        UUID authorId = UUID.fromString(authentication.getName());
        mediaService.rateMedia(name, authorId, request);
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
