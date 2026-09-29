package com.example.server.exception;

@SuppressWarnings("serial")
public class InvalidRefreshTokenException extends RuntimeException {
	public InvalidRefreshTokenException(String message) {
		super(message);
	}
}
