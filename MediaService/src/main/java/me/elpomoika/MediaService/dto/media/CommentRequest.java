package me.elpomoika.MediaService.dto.media;

import java.time.Instant;

public record CommentRequest(String commentText,
                             Instant timestamp) {
}
