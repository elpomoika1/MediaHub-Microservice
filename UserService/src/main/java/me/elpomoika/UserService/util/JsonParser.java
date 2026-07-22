package me.elpomoika.UserService.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class JsonParser {
    private final ObjectMapper objectMapper;

    public <T> T fromJson(String json, Class<T> targetType) {
        try {
            return objectMapper.readValue(json, targetType);
        } catch (JsonParseException e) {
            throw new IllegalStateException("Не удалось десериализовать JSON в " + targetType.getSimpleName(), e);
        }
    }
}
