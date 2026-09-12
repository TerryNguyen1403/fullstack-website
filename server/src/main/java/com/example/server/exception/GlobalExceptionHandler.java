package com.example.server.exception;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.server.dto.ErrorResponseDTO;
import com.example.server.dto.ValidationErrorResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
		// Lấy danh sách lỗi từ BindingResult, gộp "field: message"
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + ": " + err.getDefaultMessage()).toList();

		ValidationErrorResponseDTO response = new ValidationErrorResponseDTO(LocalDate.now(),
				HttpStatus.BAD_REQUEST.value(), "Dữ liệu đầu vào không hợp lệ", errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<ErrorResponseDTO> emailAlreadyExistsExceptionHandler(DuplicateEmailException e,
			HttpServletRequest request) {
		ErrorResponseDTO body = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
				request.getRequestURI());

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

  @ExceptionHandler(EmailNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> emailNotFoundExceptionHandler(EmailNotFoundException e, HttpServletRequest request) {
    ErrorResponseDTO body = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorResponseDTO> invalidPasswordExceptionHandler(InvalidPasswordException e, HttpServletRequest request) {
    ErrorResponseDTO body = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}
