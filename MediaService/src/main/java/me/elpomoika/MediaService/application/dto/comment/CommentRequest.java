package me.elpomoika.MediaService.application.dto.comment;

public record CommentRequest(String commentText,
                             Long parentCommentId) {
}
