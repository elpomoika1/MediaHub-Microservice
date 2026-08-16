package me.elpomoika.MediaService.application.dto.media;

import java.util.List;

import me.elpomoika.MediaService.application.dto.comment.CommentResponse;
import me.elpomoika.MediaService.domain.enums.MediaType;

public record MediaResponse(
        String name,
        String title,
        int episodesCount,
        Integer userRating,
        MediaType type,
        String imageUrl,
        double averageRating,
        List<CommentResponse> comments,
        List<EpisodeResponse> episodes,
        List<MediaPreviewResponse> recommendations
) { }
