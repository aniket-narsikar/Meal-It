package com.edu.aniket.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class ResponcseStructure<T> {
	private int status;
	private String message;
	private T data;

}
