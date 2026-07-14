package me.elpomoika.AuthenticationService.dto;

import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;
}
