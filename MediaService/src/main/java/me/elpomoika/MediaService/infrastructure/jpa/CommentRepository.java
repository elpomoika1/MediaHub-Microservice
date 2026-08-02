package me.elpomoika.MediaService.repository;

import me.elpomoika.MediaService.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> getCommentById(Long id);
}
