package me.elpomoika.MediaService.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.elpomoika.MediaService.dto.comment.VoteType;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentVote {
    @Id
    @GeneratedValue
    private Long id;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private VoteType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
