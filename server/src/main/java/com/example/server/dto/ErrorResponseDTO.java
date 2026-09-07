package com.example.server.dto;

import java.time.LocalDate;
import java.util.List;

public record ErrorResponseDTO(LocalDate timestamp, int status, String message, List<String> errors) {
}
