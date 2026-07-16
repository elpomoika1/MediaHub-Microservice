package me.elpomoika.MediaService.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue
    private Long id;

    @Min(1) @Max(10)
    private double value;

//  todo  private Long userId;

    @ManyToOne
    private Media media;
}