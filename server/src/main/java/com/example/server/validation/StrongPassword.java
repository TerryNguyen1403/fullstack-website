package com.example.server.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = StrongPasswordValidation.class) // gắn với class kiểm tra
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
	// Thông báo lỗi mặc định khi validate thất bại
	String message() default "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt";

	// Bắt buộc phải có - dùng cho validation groups
	Class<?>[] groups() default {};

	// Bắt buộc phải có - dùng cho metadata mở rộng
	Class<? extends Payload>[] payload() default {};

	// Tham số tùy chỉnh: độ dài tối thiểu (mặc định 8)
	int minLength() default 8;
}
