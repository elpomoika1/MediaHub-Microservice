package me.elpomoika.UserService.consumer.event;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;


public record UserRegisteredEvent(UUID userId,
        String email,
        String login) {
}
