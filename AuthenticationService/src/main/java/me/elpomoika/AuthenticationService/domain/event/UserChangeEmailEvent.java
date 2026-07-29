package me.elpomoika.AuthenticationService.domain.event;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class UserChangeEmailEvent {
    UUID userId;
    String newEmail;
}
