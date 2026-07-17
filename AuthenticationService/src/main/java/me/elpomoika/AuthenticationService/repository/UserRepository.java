package me.elpomoika.AuthenticationService.repository;

import me.elpomoika.AuthenticationService.domain.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(@NonNull UUID id);
    boolean existsByEmail(String email);
}