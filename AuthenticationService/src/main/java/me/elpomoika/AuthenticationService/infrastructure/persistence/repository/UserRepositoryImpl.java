package me.elpomoika.AuthenticationService.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.domain.entities.User;
import me.elpomoika.AuthenticationService.domain.exception.UserNotFoundException;
import me.elpomoika.AuthenticationService.domain.repository.UserRepository;
import me.elpomoika.AuthenticationService.infrastructure.persistence.jpa.JpaUserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public User findById(UUID id) {
        return jpaUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by userId"));
    }

    @Override
    public User findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}
