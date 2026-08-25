package com.edu.aniket.exception;

public class FoodProductWithTheGivenIdNotFound extends RuntimeException {

	private String message;

	public FoodProductWithTheGivenIdNotFound() {
	}

	public FoodProductWithTheGivenIdNotFound(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
