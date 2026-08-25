package com.edu.aniket.exception;

public class UserIdNotFoundException extends RuntimeException {

	private String message;

	public UserIdNotFoundException() {
	}

	public UserIdNotFoundException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
