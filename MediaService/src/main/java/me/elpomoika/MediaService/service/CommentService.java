package me.elpomoika.MediaService.service;

import lombok.RequiredArgsConstructor;
import me.elpomoika.MediaService.domain.entity.Comment;
import me.elpomoika.MediaService.domain.entity.CommentVote;
import me.elpomoika.MediaService.domain.entity.Media;
import me.elpomoika.MediaService.dto.comment.VoteType;
import me.elpomoika.MediaService.dto.media.CommentRequest;
import me.elpomoika.MediaService.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final MediaService mediaService;
    private final CommentRepository repository;

    public void leaveComment(UUID authorId, String mediaName, CommentRequest request) {
        Media media = mediaService.getMediaBySlug(mediaName);
        if (media == null) return;

        Comment parent = null;

        if (request.parentCommentId() != null) {
            parent = repository.findById(request.parentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
            if (!parent.getMedia().getId().equals(media.getId())) {
                throw new IllegalArgumentException("Comment belongs to another media");
            }
        }

        Comment comment = Comment.builder()
                .commentText(request.commentText())
                .parentComment(parent)
                .replies(null)
                .votes(new ArrayList<>())
                .timestamp(Instant.now())
                .media(media)
                .authorId(authorId)
                .build();

        repository.save(comment);
    }

    public void rate(UUID userId, Long commentId, VoteType type) {
        Comment comment = repository.getCommentById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        CommentVote vote = CommentVote.builder()
                .userId(userId)
                .type(type)
                .comment(comment)
                .build();

        comment.getVotes().add(vote);

        repository.save(comment);
    }
}
