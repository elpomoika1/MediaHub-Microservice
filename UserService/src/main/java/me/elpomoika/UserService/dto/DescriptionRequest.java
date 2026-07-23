package me.elpomoika.UserService.dto;

import jakarta.validation.constraints.Size;

public record DescriptionRequest(
        @Size(max = 500)
        String description) {
}
