package me.elpomoika.AuthenticationService.application.dto;

import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;
}
