package com.edu.aniket.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserIsNotValiedToAddItem extends RuntimeException {

	private String message;
	
	public String getMessage()
	{
		return message;
	}
}
