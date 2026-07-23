package me.elpomoika.UserService.consumer.event;

import java.util.UUID;

public record UserRegisteredEvent(UUID userId,
        String email,
        String login) {
}
