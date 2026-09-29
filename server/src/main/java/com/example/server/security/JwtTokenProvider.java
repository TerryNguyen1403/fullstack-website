package com.example.server.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.server.entity.enums.RoleName;
import com.example.server.exception.CustomJwtException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {
	private final SecretKey secretKey;
	@Getter
	private final long expirationMs;

	// Constructor injection
	public JwtTokenProvider(@Value("${app.jwt.secret}") String secretKey,
			@Value("${app.jwt.access-expiration-ms}") long expirationMs) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.expirationMs = expirationMs;
	}

	public String generateToken(String email, RoleName roleName) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + expirationMs);

		return Jwts.builder().subject(email).claim("role", roleName).signWith(secretKey).issuedAt(now)
				.expiration(expiration).compact();
	}

	private Claims parseClaims(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
	}

	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			// Handle token expiration
			throw new CustomJwtException("Token đã hết hạn");
		} catch (MalformedJwtException e) {
			// Handle malformed/invalid token string
			throw new CustomJwtException("Token giả mạo");
		}
	}

	public String getSubjectFromToken(String token) {
		return parseClaims(token).getSubject();
	}

	public RoleName getRoleFromToken(String token) {
		String role = parseClaims(token).get("role", String.class);
		return RoleName.valueOf(role);
	}
}
