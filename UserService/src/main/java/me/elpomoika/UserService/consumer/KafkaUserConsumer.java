package me.elpomoika.UserService.consumer;

import lombok.RequiredArgsConstructor;
import me.elpomoika.UserService.consumer.event.UserRegisteredEvent;
import me.elpomoika.UserService.service.ProfileService;
import me.elpomoika.UserService.util.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class KafkaUserConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaUserConsumer.class);

    private final JsonParser jsonParser;
    private final ProfileService profileService;

    @KafkaListener(topics = "user-events", groupId = "users")
    public void handleRegister(ConsumerRecord<String, String> record) {
        String eventType = extractEventType(record);

        switch (eventType) {
            case "user.registered":
                UserRegisteredEvent registeredEvent = jsonParser.fromJson(record.value(), UserRegisteredEvent.class);
                profileService.createProfile(registeredEvent);
                break;
            default:
                LOGGER.warn("Неизвестный тип события: {}", eventType);
        }
    }

    private String extractEventType(ConsumerRecord<String, String> record) {
        Header header = record.headers().lastHeader("eventType");
        if (header == null) {
            throw new IllegalStateException("Отсутствует заголовок eventType в сообщении");
        }
        return new String(header.value(), StandardCharsets.UTF_8);
    }
}