package com.edu.aniket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dto.DashboardStatsDto;
import com.edu.aniket.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {

	private final DashboardService dashboardService;

	@Autowired
	public DashBoardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/display")
	public ResponseEntity<ResponseStructure<String>> display() {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData("Welcome to Meal-It API Service");
		responseStructure.setMessage("Dashboard Information Retrieved");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	@GetMapping("/stats")
	public ResponseEntity<ResponseStructure<DashboardStatsDto>> getDashboardStats() {
		return dashboardService.getDashboardStats();
	}
}
