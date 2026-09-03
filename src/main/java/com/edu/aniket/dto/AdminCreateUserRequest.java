package com.edu.aniket.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AdminCreateUserRequest {

	@NotEmpty(message = "User Name Can't be Empty")
	private String name;

	@NotEmpty(message = "Email is required")
	@Email(message = "Invalid Email Id")
	private String email;

	@Min(value = 6000000000L, message = "Phone number must be valid 10 digits")
	@Max(value = 9999999999L, message = "Phone number must be valid 10 digits")
	@JsonAlias({"phone_number", "phoneNumber"})
	private long phoneNumber;

	@NotEmpty(message = "Password is required")
	private String password;

	public AdminCreateUserRequest() {
	}

	public AdminCreateUserRequest(String name, String email, long phoneNumber, String password) {
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
