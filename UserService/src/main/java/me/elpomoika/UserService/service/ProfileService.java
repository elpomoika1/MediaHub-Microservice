package me.elpomoika.UserService.service;

import lombok.RequiredArgsConstructor;
import me.elpomoika.UserService.consumer.event.UserRegisteredEvent;
import me.elpomoika.UserService.domain.entity.UserProfile;
import me.elpomoika.UserService.dto.ProfileResponse;
import me.elpomoika.UserService.mapper.ProfileMapper;
import me.elpomoika.UserService.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public void createProfile(UserRegisteredEvent event) {
        UserProfile profile = UserProfile.builder()
                .login(event.login())
                .userId(event.userId())
                .email(event.email())
                .nickname(event.login())
                .description(null)
                .avatarUrl(null)
                .createdAt(Instant.now())
                .build();

        profileRepository.save(profile);
    }

    public void setNickname(String nickname, UUID userId) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow();

        profile.setNickname(nickname);

        profileRepository.save(profile);
    }

    public ProfileResponse getProfile(UUID userId) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow();

        return profileMapper.toDto(profile);
    }

    public ProfileResponse getProfile(String login) {
        UserProfile profile = profileRepository.findByLogin(login)
                .orElseThrow();

        return profileMapper.toDto(profile);
    }
}
