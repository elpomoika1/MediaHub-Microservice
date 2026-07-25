package me.elpomoika.MediaService.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerSource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String providerName;
    private String quality;

    @Column(length = 2048)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;
}
