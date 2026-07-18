package me.elpomoika.AuthenticationService.event;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserRegisteredEvent {
    UUID userId;
    String email;
    String login;
}
