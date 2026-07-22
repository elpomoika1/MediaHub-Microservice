package me.elpomoika.UserService.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProfileResponse {
    String nickname;
    String avatarUrl;
    String description;
}
