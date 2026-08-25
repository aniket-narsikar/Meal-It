package com.edu.aniket.exception;

public class FoodOrderNotFoundException extends RuntimeException {

	private String message;

	public FoodOrderNotFoundException() {
	}

	public FoodOrderNotFoundException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
