package me.elpomoika.AuthenticationService.infrastructure.persistence.jpa;

import me.elpomoika.AuthenticationService.domain.entities.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(@NonNull UUID id);
    boolean existsByEmail(String email);
}