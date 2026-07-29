package me.elpomoika.AuthenticationService.application.usecases;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.elpomoika.AuthenticationService.application.dto.UserDto;
import me.elpomoika.AuthenticationService.application.dto.auth.RegisterRequest;
import me.elpomoika.AuthenticationService.domain.entities.User;
import me.elpomoika.AuthenticationService.domain.enums.Role;
import me.elpomoika.AuthenticationService.domain.exception.UserExistsException;
import me.elpomoika.AuthenticationService.domain.repository.UserRepository;
import me.elpomoika.AuthenticationService.domain.event.UserRegisteredEvent;
import me.elpomoika.AuthenticationService.infrastructure.outbox.OutboxService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class RegisterUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OutboxService outboxService;

    @Transactional
    public UserDto execute(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) throw new UserExistsException("User with this email already exists");

        User user = new User();
        user.setRoles(Collections.singleton(Role.MEMBER));
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);

        UserRegisteredEvent payload = UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .build();

        outboxService.publish(payload, "user.registered", user.getId());

        return UserDto.builder()
                .email(saved.getEmail())
                .login(saved.getLogin())
                .build();
    }
}
