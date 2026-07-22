package me.elpomoika.UserService.mapper;

import me.elpomoika.UserService.domain.entity.UserProfile;
import me.elpomoika.UserService.dto.ProfileResponse;
import org.springframework.stereotype.Service;

@Service
public class ProfileMapper {
    public ProfileResponse toDto(UserProfile profile) {
        return ProfileResponse.builder()
                .nickname(profile.getNickname())
                .description(profile.getDescription())
                .avatarUrl(profile.getAvatarUrl())
                .build();
    }
}
