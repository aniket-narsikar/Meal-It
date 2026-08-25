package com.edu.aniket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.dao.UserDao;
import com.edu.aniket.entity.User;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {

	@Autowired
	private UserDao dao;

	@GetMapping("/display")
	public String display() {
		return "Welcome to Meal-It API Service";
	}

	@GetMapping("/login")
	public User checkUserDao() {
		User user = new User();
		user.setEmail("sachin@java.com");
		user.setName("Sachin");
		user.setPassword("Sachin#123");
		return user;
	}
}
