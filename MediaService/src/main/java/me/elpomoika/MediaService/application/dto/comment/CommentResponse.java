package me.elpomoika.MediaService.application.dto.comment;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CommentResponse(
        Long id,
        UUID authorId,
        String commentText,
        Instant timestamp,
        Long parentCommentId,
        List<CommentResponse> replies
) {
}
