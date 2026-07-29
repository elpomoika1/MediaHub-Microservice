package me.elpomoika.AuthenticationService.infrastructure.serialization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class JsonParser {
    private final ObjectMapper objectMapper;

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JacksonException e) {
            throw new IllegalStateException("Не удалось сериализовать событие в JSON", e);
        }
    }
}
