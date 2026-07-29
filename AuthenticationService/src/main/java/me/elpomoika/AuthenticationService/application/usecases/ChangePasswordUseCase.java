package me.elpomoika.AuthenticationService.application.usecases;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.application.dto.UserDto;
import me.elpomoika.AuthenticationService.application.dto.auth.ChangePasswordRequest;
import me.elpomoika.AuthenticationService.domain.entities.User;
import me.elpomoika.AuthenticationService.domain.exception.UserNotFoundException;
import me.elpomoika.AuthenticationService.infrastructure.persistence.jpa.JpaUserRepository;
import me.elpomoika.AuthenticationService.infrastructure.persistence.repository.UserRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangePasswordUseCase {
    private final UserRepositoryImpl userRepository;

    public UserDto execute(ChangePasswordRequest request, UUID userId) {
        User user = userRepository.findById(userId);

        user.setPassword(request.password());

        userRepository.save(user);

        return UserDto.builder()
                .email(user.getEmail())
                .login(user.getLogin())
                .build();
    }
}
