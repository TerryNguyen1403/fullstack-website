package com.example.server.dto;

import org.springframework.validation.annotation.Validated;

import com.example.server.entity.enums.Role;
import com.example.server.entity.enums.UserGender;
import com.example.server.entity.enums.UserStatus;
import com.example.server.validation.StrongPassword;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Validated
public record UserCreationDTO(@NotBlank(message = "Tên không được để trống") String firstName,
		@NotBlank(message = "Tên không được để trống") String lastName, UserGender gender,
		@Email(message = "Vui lòng nhập email đúng định dạng") @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Vui lòng nhập email đúng định dạng") String email,
		@NotBlank(message = "Mật khẩu không được để trống") @StrongPassword String password,
		@NotBlank(message = "Số điện thoại không được để trống") String phoneNumber,
		@JsonSetter(nulls = Nulls.SKIP) UserStatus status, com.example.server.entity.enums.Role role) {
	public UserCreationDTO {
		if (status == null) {
			status = UserStatus.ACTIVE;
		}
		role = Role.USER;
	}
}
