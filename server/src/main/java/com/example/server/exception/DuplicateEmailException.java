package com.example.server.exception;

@SuppressWarnings("serial")
public class DuplicateEmailException extends RuntimeException {
	public DuplicateEmailException(String message) {
		super(message);
	}
}
