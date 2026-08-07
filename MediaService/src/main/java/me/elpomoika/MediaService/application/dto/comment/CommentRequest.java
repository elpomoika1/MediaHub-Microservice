package me.elpomoika.MediaService.application.dto.comment;

import java.time.Instant;

public record CommentRequest(String commentText,
                             Long parentCommentId) {
}
