package com.example.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.server.dto.LoginRequestDTO;
import com.example.server.dto.LoginResponseDTO;
import com.example.server.entity.User;
import com.example.server.entity.enums.RoleName;
import com.example.server.exception.EmailNotFoundException;
import com.example.server.repository.UserRepository;
import com.example.server.security.JwtTokenProvider;

@Validated
@Service
public class AuthService {
	@Value("${JWT_ACCESS_EXPIRATION}")
	private long accessTokenExpiryMs;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;

	public AuthService(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager,
			UserRepository userRepository) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
	}

	public LoginResponseDTO login(LoginRequestDTO request) {
		// 0. Kiểm tra email
		// Nếu tồn tại -> tiếp tục flow else throw exception
		User found = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new EmailNotFoundException("Email không tồn tại"));

		// 1. Đóng gói email + password thành một "yêu cầu xác thực"
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.email(),
				request.password());

		// 2. Giao cho AuthenticationManager kiểm tra,
		// nếu sai throw BadCredentialException
		Authentication authentication = authenticationManager.authenticate(authToken);

		// 3. Xác thực thành công, lấy user đã được nạp
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		assert userDetails != null;
		RoleName roleName = found.getRole().getRoleName();

		// 4. Sinh Jwt Token
		String jwt = jwtTokenProvider.generateToken(userDetails.getUsername(), roleName);

		return new LoginResponseDTO(jwt);
	}

	public RoleName getRoleFromToken(String token) {
		String validType = token.replace("Bearer ", "");
		return jwtTokenProvider.getRoleFromToken(validType);
	}
}
