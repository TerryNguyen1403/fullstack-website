package com.example.server.dto;

import com.example.server.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Vui lòng nhập email")
        String email,
        @NotBlank(message = "Mật khẩu không được để trống")
        @StrongPassword
        String password) {
}
