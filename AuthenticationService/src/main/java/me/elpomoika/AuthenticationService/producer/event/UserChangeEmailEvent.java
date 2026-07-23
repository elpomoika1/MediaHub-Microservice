package me.elpomoika.AuthenticationService.producer.event;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserChangeEmailEvent {
    UUID userId;
    String newEmail;
}
