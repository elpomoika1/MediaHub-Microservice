package me.elpomoika.UserService.service;

import lombok.RequiredArgsConstructor;
import me.elpomoika.UserService.consumer.event.UserRegisteredEvent;
import me.elpomoika.UserService.domain.entity.UserProfile;
import me.elpomoika.UserService.dto.DescriptionRequest;
import me.elpomoika.UserService.dto.ProfileResponse;
import me.elpomoika.UserService.dto.UsernameRequest;
import me.elpomoika.UserService.exception.file.FileProcessingException;
import me.elpomoika.UserService.exception.file.FileTooLargeException;
import me.elpomoika.UserService.exception.file.InvalidFileTypeException;
import me.elpomoika.UserService.exception.user.UserExistsException;
import me.elpomoika.UserService.exception.user.UserNotFoundException;
import me.elpomoika.UserService.mapper.ProfileMapper;
import me.elpomoika.UserService.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    private static final int MAX_UPLOAD_SIZE_MB = 5 * 1024 * 1024;

    public void createProfile(UserRegisteredEvent event) {
        if (profileRepository.existsByEmail(event.email())) throw new UserExistsException("User with the same email already exists");

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

    public void setNickname(UsernameRequest request, UUID userId) {
        UserProfile profile = getProfileById(userId);

        profile.setNickname(request.nickname());
        profileRepository.save(profile);
    }

    public void setDescription(DescriptionRequest request, UUID userId) {
        UserProfile profile = getProfileById(userId);

        profile.setDescription(request.description());
        profileRepository.save(profile);
    }

    public void setAvatar(MultipartFile file, UUID userId) {
        try {
            if (file.isEmpty()) throw new InvalidFileTypeException("File is empty");
            if (file.getSize() > MAX_UPLOAD_SIZE_MB) throw new FileTooLargeException("Maximum file size is 5 MB");
            if (ImageIO.read(file.getInputStream()) == null) throw new InvalidFileTypeException("File isn't an image");

            UserProfile profile = getProfileById(userId);

            profile.setAvatarUrl("todo");
            profileRepository.save(profile);
        } catch (IOException e) {
            throw new FileProcessingException("Failed to read uploaded file", e);
        }
    }

    public ProfileResponse getProfile(UUID userId) {
        UserProfile profile = getProfileById(userId);

        return profileMapper.toDto(profile);
    }

    public ProfileResponse getProfile(String login) {
        UserProfile profile = profileRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found by user login"));

        return profileMapper.toDto(profile);
    }

    private UserProfile getProfileById(UUID userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found by user id"));
    }
}
