package com.edu.aniket.exception;

public class UserIsNotValidToAddItemException extends RuntimeException {

	public UserIsNotValidToAddItemException() {
	}

	public UserIsNotValidToAddItemException(String message) {
		super(message);
	}
}
