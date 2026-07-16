package me.elpomoika.MediaService.util;

import lombok.experimental.UtilityClass;
import me.elpomoika.MediaService.domain.entity.Rating;

import java.util.List;

@UtilityClass
public class ArithmeticMeanCalculator {
    public double calculateAverage(List<Rating> rating) {
        return rating.stream()
                .mapToDouble(Rating::getValue)
                .average()
                .orElse(0.0);
    }
}
