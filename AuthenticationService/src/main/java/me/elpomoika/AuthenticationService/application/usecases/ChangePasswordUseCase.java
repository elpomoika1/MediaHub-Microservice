package me.elpomoika.AuthenticationService.application.usecases;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.application.dto.UserDto;
import me.elpomoika.AuthenticationService.application.dto.auth.ChangePasswordRequest;
import me.elpomoika.AuthenticationService.domain.entities.User;
import me.elpomoika.AuthenticationService.infrastructure.persistence.repository.UserRepositoryAdapter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangePasswordUseCase {
    private final UserRepositoryAdapter userRepository;

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
