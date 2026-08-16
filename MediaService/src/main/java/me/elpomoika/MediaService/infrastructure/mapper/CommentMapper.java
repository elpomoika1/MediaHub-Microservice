package me.elpomoika.MediaService.infrastructure.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import me.elpomoika.MediaService.application.dto.comment.CommentResponse;
import me.elpomoika.MediaService.domain.entity.Comment;

@Component
public class CommentMapper {
    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getAuthorId(),
            comment.getCommentText(),
            comment.getTimestamp(),
            comment.getParentComment() != null ? comment.getParentComment().getId(): null,
            comment.getReplies() == null
                ? List.of()
                : comment.getReplies().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    public List<CommentResponse> toResponse(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return List.of();
        }

        return comments.stream()
                .map(this::toResponse)
                .toList();
    }
}
