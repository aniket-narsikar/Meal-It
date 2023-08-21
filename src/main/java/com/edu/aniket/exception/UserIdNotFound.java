package com.edu.aniket.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public class UserIdNotFound extends RuntimeException {
private String message;

@Override
public String getMessage() {
	
	return message;
}
}
