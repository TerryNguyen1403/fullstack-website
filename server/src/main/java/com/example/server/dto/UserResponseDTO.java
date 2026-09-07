package com.example.server.dto;

import java.time.LocalDate;

import com.example.server.entity.enums.Role;
import com.example.server.entity.enums.UserGender;
import com.example.server.entity.enums.UserStatus;

public record UserResponseDTO(long id, String firstName, String lastName, UserGender gender, String email,
		String phoneNumber, Role role, UserStatus status, LocalDate createdAt, LocalDate updatedAt) {

}
