package me.elpomoika.AuthenticationService.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {
    private String login;
    private String email;
    private String password;
}
