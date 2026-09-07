package com.example.server.exception;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.server.dto.ErrorResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
		// Lấy danh sách lỗi từ BindingResult, gộp "field: message"
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + ": " + err.getDefaultMessage()).toList();

		ErrorResponseDTO response = new ErrorResponseDTO(LocalDate.now(), HttpStatus.BAD_REQUEST.value(),
				"Dữ liệu đầu vào không hợp lệ", errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
