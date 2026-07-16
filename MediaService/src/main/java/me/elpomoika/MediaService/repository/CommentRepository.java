package me.elpomoika.MediaService.repository;

import me.elpomoika.MediaService.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
