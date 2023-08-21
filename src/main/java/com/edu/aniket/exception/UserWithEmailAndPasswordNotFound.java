package com.edu.aniket.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserWithEmailAndPasswordNotFound extends RuntimeException {

	private String message;

	@Override
	public String getMessage() {
		return message;
	}
}
