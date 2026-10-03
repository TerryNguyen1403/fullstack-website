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
import com.example.server.exception.CustomRefreshTokenException;
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
		refreshToken.setTokenHash(sha256(rawToken));
		refreshToken.setCreatedAt(now);
		refreshToken.setExpiryDate(now.plus(ttl));
		refreshToken.setUser(user);
		refreshTokenRepository.save(refreshToken);

		return new RefreshTokenResponseDto(rawToken, refreshToken.getExpiryDate());
	}

	public RefreshToken validate(String token) {
		// 1. Hash token trước khi query
		String tokenHash = sha256(token);

		// 2. Query và trả về entity
		// Nếu sai -> throw exception
		RefreshToken found = refreshTokenRepository.findByTokenHash(tokenHash)
				.orElseThrow(() -> new CustomRefreshTokenException("Token không hợp lệ"));

		// 3.Validate
		// 3.1 Throw exception nếu isRevoked = true
		if (found.isRevoked())
			throw new CustomRefreshTokenException("Token đã bị thu hồi");

		// 3.2 Throw exception nếu hết hạn
		if (found.getExpiryDate().isBefore(Instant.now()))
			throw new CustomRefreshTokenException("Token hết hạn");

		// 4. Không xảy ra bất cứ exception nào -> trả về true
		return found;

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
