package me.elpomoika.MediaService.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import me.elpomoika.MediaService.domain.enums.Genre;
import me.elpomoika.MediaService.domain.enums.MediaType;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.util.ArrayList;
import java.util.List;

@Entity @Indexed
@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @FullTextField(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @ElementCollection(targetClass = Genre.class)
    @Enumerated(EnumType.STRING)
    private List<Genre> genres;

    @Column(length = 2048)
    private String imageUrl;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL)
    private final List<Rating> rating = new ArrayList<>();

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();
}
