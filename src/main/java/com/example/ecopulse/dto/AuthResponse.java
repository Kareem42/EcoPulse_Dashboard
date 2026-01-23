package com.example.ecopulse.dto;

import lombok.Data;
import lombok.Builder;


@Data
@Builder
public class AuthResponse {
    private String token;
    private UserResponse user;
}
