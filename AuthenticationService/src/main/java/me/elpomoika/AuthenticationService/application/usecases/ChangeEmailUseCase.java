package me.elpomoika.AuthenticationService.application.usecases;

import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.application.dto.UserDto;
import me.elpomoika.AuthenticationService.application.dto.auth.ChangeEmailRequest;
import me.elpomoika.AuthenticationService.domain.entities.User;
import me.elpomoika.AuthenticationService.infrastructure.persistence.repository.UserRepositoryAdapter;
import me.elpomoika.AuthenticationService.domain.event.UserChangeEmailEvent;
import me.elpomoika.AuthenticationService.infrastructure.outbox.OutboxService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangeEmailUseCase {
    private final UserRepositoryAdapter userRepository;
    private final OutboxService outboxService;

    public UserDto execute(ChangeEmailRequest request, UUID userId) {
        User user = userRepository.findByEmail(request.email());

        user.setEmail(request.email());
        userRepository.save(user);

        UserChangeEmailEvent changeEmailEvent = UserChangeEmailEvent.builder()
                .userId(userId)
                .newEmail(request.email())
                .build();

        outboxService.publish(changeEmailEvent, "user.change.email", userId);

        return UserDto.builder()
                .email(user.getEmail())
                .login(user.getLogin())
                .build();
    }
}
