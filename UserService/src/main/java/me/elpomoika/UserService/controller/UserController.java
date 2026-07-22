package me.elpomoika.UserService.controller;

import lombok.RequiredArgsConstructor;
import me.elpomoika.UserService.dto.ProfileResponse;
import me.elpomoika.UserService.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final ProfileService profileService;

    @PostMapping("/nickname")
    @PreAuthorize("hasRole('MEMBER')")
    public void handleNickname(@RequestBody String nickname, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());

        profileService.setNickname(nickname, userId);
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