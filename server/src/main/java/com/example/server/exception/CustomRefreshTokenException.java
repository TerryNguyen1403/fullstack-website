package com.example.server.exception;

@SuppressWarnings("serial")
public class CustomRefreshTokenException extends RuntimeException {
	public CustomRefreshTokenException(String msg) {
		super(msg);
	}

}
