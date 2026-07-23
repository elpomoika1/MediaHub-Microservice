package me.elpomoika.UserService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.elpomoika.UserService.dto.DescriptionRequest;
import me.elpomoika.UserService.dto.ProfileResponse;
import me.elpomoika.UserService.dto.UsernameRequest;
import me.elpomoika.UserService.service.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final ProfileService profileService;

    @PostMapping("/nickname")
    @PreAuthorize("hasRole('MEMBER')")
    public void handleNickname(@Valid @RequestBody UsernameRequest request, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());

        profileService.setNickname(request, userId);
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MEMBER')")
    public void handleAvatar(@RequestParam("file") MultipartFile file, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());

        profileService.setAvatar(file, userId);
    }

    @PostMapping("/description")
    @PreAuthorize("hasRole('MEMBER')")
    public void handleDescription(@Valid @RequestBody DescriptionRequest request, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());

        profileService.setDescription(request, userId);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> handleProfile(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());

        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @GetMapping("/profile/{login}")
    public ResponseEntity<ProfileResponse> handleUserProfile(@PathVariable String login) {
        return ResponseEntity.ok(profileService.getProfile(login));
    }
}