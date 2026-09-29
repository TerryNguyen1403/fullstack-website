package com.example.server.controller;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dto.LoginRequestDTO;
import com.example.server.dto.LoginResponseDto;
import com.example.server.dto.LoginResultDto;
import com.example.server.entity.RefreshToken;
import com.example.server.entity.enums.RoleName;
import com.example.server.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	private final boolean cookieSecure;

	public AuthController(AuthService authService, @Value("${app.cookie.secure}") boolean cookieSecure) {
		this.authService = authService;
		this.cookieSecure = cookieSecure;
	}

	@PostMapping("/log-in")
	public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDTO request) {
		LoginResultDto result = authService.login(request);

		// 1. Gắn Refresh Token vào Cookie
		ResponseCookie cookie = ResponseCookie.fromClientResponse("refresh-token", result.refreshToken()).httpOnly(true)
				.secure(cookieSecure).sameSite("Strict").path("/api/auth")
				.maxAge(Duration.between(Instant.now(), result.expiryDate())).build();

		// 2. Gắn Access Token vào body và trả Response về Client
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new LoginResponseDto(result.token()));
	}

	@GetMapping("/validate")
	public ResponseEntity<String> validate(@CookieValue(name = "refresh-token") String refreshToken) {
		RefreshToken tokenEntity = authService.validate(refreshToken);
		return ResponseEntity.ok("Token hợp lệ cho user: " + tokenEntity.getUser().getEmail());
	}

	@GetMapping("/getRole")
	public ResponseEntity<RoleName> getRoleFromToken(@RequestHeader("Authorization") String jwtToken) {
		return ResponseEntity.status(200).body(authService.getRoleFromToken(jwtToken));
	}
}
