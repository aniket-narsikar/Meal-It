package com.edu.aniket.exception;

public class UserWithEmailAndPasswordNotFound extends RuntimeException {

	private String message;

	public UserWithEmailAndPasswordNotFound() {
	}

	public UserWithEmailAndPasswordNotFound(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
