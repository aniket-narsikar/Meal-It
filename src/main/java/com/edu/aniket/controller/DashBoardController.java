package com.edu.aniket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.config.ResponseStructure;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {

	@GetMapping("/display")
	public ResponseEntity<ResponseStructure<String>> display() {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData("Welcome to Meal-It API Service");
		responseStructure.setMessage("Dashboard Information Retrieved");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}
}
