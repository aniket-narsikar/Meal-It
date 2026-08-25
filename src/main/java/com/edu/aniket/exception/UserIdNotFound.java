package com.edu.aniket.exception;

public class UserIdNotFound extends RuntimeException {

	private String message;

	public UserIdNotFound() {
	}

	public UserIdNotFound(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
