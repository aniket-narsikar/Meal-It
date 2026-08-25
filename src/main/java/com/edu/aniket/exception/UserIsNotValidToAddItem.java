package com.edu.aniket.exception;

public class UserIsNotValidToAddItem extends RuntimeException {

	private String message;

	public UserIsNotValidToAddItem() {
	}

	public UserIsNotValidToAddItem(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
