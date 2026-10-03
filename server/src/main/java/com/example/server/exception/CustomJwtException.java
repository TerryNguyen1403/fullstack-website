package com.example.server.exception;

@SuppressWarnings("serial")
public class CustomJwtException extends RuntimeException {
	public CustomJwtException(String message) {
		super(message);
	}
}
