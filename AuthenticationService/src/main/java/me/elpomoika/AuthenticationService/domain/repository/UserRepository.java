package me.elpomoika.AuthenticationService.domain.repository;

import me.elpomoika.AuthenticationService.domain.entities.User;

import java.util.UUID;

public interface UserRepository {
    User save(User user);
    User findById(UUID id);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
