package me.elpomoika.UserService.repository;

import me.elpomoika.UserService.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(UUID userId);
    Optional<UserProfile> findByLogin(String login);
    boolean existsByEmail(String email);
}
