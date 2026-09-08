package com.example.server.dto;

import java.time.LocalDate;

public record ErrorResponseDTO(int status, String message, String path, LocalDate timestamp) {
	public ErrorResponseDTO(int status, String message, String path) {
		this(status, message, path, LocalDate.now());
	}
}
