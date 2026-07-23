package me.elpomoika.UserService.dto;

import jakarta.validation.constraints.Size;

public record UsernameRequest(
        @Size(max = 20)
        String nickname) {
}
