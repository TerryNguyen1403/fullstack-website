package com.example.server.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidation implements ConstraintValidator<StrongPassword, String> {
	private int minLength;

	// Regex
	private static final Pattern HAS_LOWER = Pattern.compile("[a-z]");
	private static final Pattern HAS_UPPER = Pattern.compile("[A-Z]");
	private static final Pattern HAS_DIGIT = Pattern.compile("\\d");
	private static final Pattern HAS_SPECIAL = Pattern.compile("[^a-zA-Z0-9]");

	@Override
	public void initialize(StrongPassword annotation) {
		// Đọc tham số từ annotation khi khởi tạo
		this.minLength = annotation.minLength();
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password == null)
			return true;

		StringBuilder errors = new StringBuilder();
		if (password.length() < minLength)
			errors.append("ít nhất " + minLength + " ký tự; ");
		if (!HAS_UPPER.matcher(password).find())
			errors.append("một chữ hoa; ");
		if (!HAS_DIGIT.matcher(password).find())
			errors.append("một chữ số; ");
		if (!HAS_SPECIAL.matcher(password).find())
			errors.append("một ký tự đặc biệt; ");
		if (!HAS_LOWER.matcher(password).find())
			errors.append("một chữ thường; ");

		if (errors.length() > 0) {
			// Tắt thông báo mặc định, đặt thông báo tùy chỉnh
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Mật khẩu còn thiếu: " + errors).addConstraintViolation();
			return false;
		}
		return true;
	}
}
