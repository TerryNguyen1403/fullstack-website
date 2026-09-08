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
import com.example.server.repository.UserRepository;

@Service
@Validated
public class UserService {
	// Constructor injection
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable)
				.map(user -> new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(),
						user.getGender(), user.getEmail(), user.getPhoneNumber(), user.getRole(), user.getStatus(),
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
		user.setFirstName(request.firstName());
		user.setLastName(request.lastName());
		user.setEmail(request.email());
		user.setGender(request.gender());
		user.setPhoneNumber(request.phoneNumber());
		user.setRole(request.role());
		user.setStatus(request.status());
		user.setCreatedAt(LocalDate.now());
		user.setUpdatedAt(LocalDate.now());
		user.setPassword(hashed);
		User saved = userRepository.save(user);

		UserResponseDTO res = new UserResponseDTO(saved.getId(), saved.getFirstName(), saved.getLastName(),
				saved.getGender(), saved.getEmail(), saved.getPhoneNumber(), saved.getRole(), saved.getStatus(),
				saved.getCreatedAt(), saved.getUpdatedAt());

		return res;
	}
}
