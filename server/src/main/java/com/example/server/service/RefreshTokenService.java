package com.example.server.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.dto.RefreshTokenResponseDto;
import com.example.server.entity.RefreshToken;
import com.example.server.entity.User;
import com.example.server.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final Duration ttl;
	private final SecureRandom secureRandom = new SecureRandom();

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
			@Value("${app.jwt.refresh-token-ttl-days}") long ttlDays) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.ttl = Duration.ofDays(ttlDays);
	}

	@Transactional
	public RefreshTokenResponseDto create(User user) {
		String rawToken = generateRawToken();
		Instant now = Instant.now();

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(sha256(rawToken));
		refreshToken.setCreatedAt(now);
		refreshToken.setExpiryDate(now.plus(ttl));
		refreshToken.setUser(user);
		refreshTokenRepository.save(refreshToken);

		return new RefreshTokenResponseDto(rawToken, refreshToken.getExpiryDate());
	}

	private String generateRawToken() {
		byte[] bytes = new byte[32];
		secureRandom.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}

	private String sha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return HexFormat.of().formatHex(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
}
