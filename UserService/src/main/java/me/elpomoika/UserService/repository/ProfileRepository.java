package me.elpomoika.UserService.repository;

import me.elpomoika.UserService.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<UserProfile, Long> {
}
