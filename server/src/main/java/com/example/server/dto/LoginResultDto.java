package com.example.server.dto;

import java.time.Instant;

public record LoginResultDto(String token, String type, String refreshToken, Instant expiryDate) {
	public LoginResultDto(String token, String refreshToken, Instant expiryDate) {
		this(token, "Bearer", refreshToken, expiryDate);
	}
}
