package com.example.server.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.server.dto.UserCreationDTO;
import com.example.server.dto.UserResponseDTO;
import com.example.server.entity.User;
import com.example.server.exception.DuplicateEmailException;
import com.example.server.repository.RoleRepository;
import com.example.server.repository.UserRepository;
import com.example.server.security.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Validated
public class UserService {
	// Constructor injection
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			JwtTokenProvider jwtTokenProvider, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable)
				.map(user -> new UserResponseDTO(user.getId(), user.getFullName(), user.getGender(), user.getEmail(),
						user.getPhoneNumber(), user.getRole().getRoleName().name(), user.getStatus(),
						user.getCreatedAt(), user.getUpdatedAt()));
	}

	// Create
	@Transactional
	public UserResponseDTO createUser(UserCreationDTO request) {
		boolean existing = userRepository.existsByEmail(request.email());
		if (existing)
			throw new DuplicateEmailException(
					String.format("Email: %s đã tồn tại trong cơ sở dữ liệu", request.email()));

		User user = new User();
		String hashed = passwordEncoder.encode(request.password());
		user.setFullName(request.fullName());
		user.setEmail(request.email());
		user.setGender(request.gender());
		user.setPhoneNumber(request.phoneNumber());
		user.setRole(request.role());
		user.setStatus(request.status());
		user.setCreatedAt(LocalDate.now());
		user.setUpdatedAt(LocalDate.now());
		user.setPassword(hashed);
		User saved = userRepository.save(user);

		return new UserResponseDTO(saved.getId(), saved.getFullName(), saved.getGender(), saved.getEmail(),
				saved.getPhoneNumber(), saved.getRole().getRoleName().name(), saved.getStatus(), saved.getCreatedAt(),
				saved.getUpdatedAt());
	}
}
