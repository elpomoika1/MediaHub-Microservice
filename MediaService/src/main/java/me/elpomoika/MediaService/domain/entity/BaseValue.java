package me.elpomoika.MediaService.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@MappedSuperclass
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseValue {
    @Id
    @GeneratedValue
    private Long id;

    @Min(1) @Max(10)
    private double value;

    @Column
    private UUID userId;

    @ManyToOne
    private Media media;
}
