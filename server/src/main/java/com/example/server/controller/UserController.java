package com.example.server.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dto.UserCreationDTO;
import com.example.server.dto.UserResponseDTO;
import com.example.server.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
	// Constructor injection
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping()
	public Page<UserResponseDTO> getAllUsers(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		return userService.getAllUsers(pageable);
	}

	@PostMapping
	public ResponseEntity<UserResponseDTO> addNewUser(@Valid @RequestBody UserCreationDTO request) {
		UserResponseDTO res = userService.createUser(request);

		return ResponseEntity.status(200).body(res);
	}
}
