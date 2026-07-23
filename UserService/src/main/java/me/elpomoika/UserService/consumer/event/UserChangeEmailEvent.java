package me.elpomoika.UserService.consumer.event;

import java.util.UUID;

public record UserChangeEmailEvent(
        UUID userId,
        String newEmail) { }
