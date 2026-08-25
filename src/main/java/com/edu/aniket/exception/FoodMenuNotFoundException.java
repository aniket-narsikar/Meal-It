package com.edu.aniket.exception;

public class FoodMenuNotFoundException extends RuntimeException {

	private String message;

	public FoodMenuNotFoundException() {
	}

	public FoodMenuNotFoundException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
