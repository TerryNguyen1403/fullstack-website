package com.example.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.server.entity.RefreshToken;
import com.example.server.entity.Role;
import com.example.server.entity.User;
import com.example.server.entity.enums.RoleName;
import com.example.server.security.JwtTokenProvider;
import com.example.server.service.AuthService;
import com.example.server.service.RefreshTokenService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	@Mock
	private RefreshTokenService refreshTokenService;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@InjectMocks
	private AuthService authService;

	private static final String RAW_TOKEN = "RAW_REFRESH_TOKEN";
	private static final String NEW_ACCESS_TOKEN = "NEW_ACCESS_TOKEN";

	@Test
	void refresh_shouldReturnAccessToken_whenTokenIsValid() {
		// Arrange
		Role role = new Role();
		role.setRoleName(RoleName.USER);

		User user = new User();
		user.setEmail("example@gmail.com");
		user.setRole(role);

		RefreshToken validToken = new RefreshToken();
		validToken.setUser(user);

		when(refreshTokenService.validate(RAW_TOKEN)).thenReturn(validToken);
		when(jwtTokenProvider.generateToken(user.getEmail(), user.getRole().getRoleName()))
				.thenReturn(NEW_ACCESS_TOKEN);

		// Act
		String result = authService.refresh(RAW_TOKEN);

		// Assert
		assertThat(result).isEqualTo(NEW_ACCESS_TOKEN);
	}
}