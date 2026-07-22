package me.elpomoika.AuthenticationService.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

@Data
@AllArgsConstructor
@Value
public class RegisterRequest {
    String login;
    String email;
    String password;
}
