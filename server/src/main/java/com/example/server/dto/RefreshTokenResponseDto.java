package com.example.server.dto;

import java.time.Instant;

public record RefreshTokenResponseDto(String rawToken, Instant expiryDate) {

}
