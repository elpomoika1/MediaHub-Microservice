package me.elpomoika.MediaService.util;

import lombok.experimental.UtilityClass;
import me.elpomoika.MediaService.domain.entity.BaseValue;

import java.util.List;

@UtilityClass
public class ArithmeticMeanCalculator {
    public double calculateAverage(List<? extends BaseValue> values) {
        return values.stream()
                .mapToDouble(BaseValue::getValue)
                .average()
                .orElse(0.0);
    }
}
