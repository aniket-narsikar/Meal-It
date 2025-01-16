package com.edu.aniket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.dao.UserDao;
import com.edu.aniket.entity.User;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {
	
	private UserDao dao;
	
	@GetMapping("/display")
public String display()
{
return "Welcome to Meal It App";
}
	@GetMapping("/login")
	public User checkUserDao()
	{
		User user=new User();
		user.setEmail("sachin@java.com");
		user.setName("Sachin");
		user.setPassword("Sachin#123");
		dao.saveUser(user);
		return dao.findUserByEmailAndPassword("sachin@java.com", "Sachin#123");
	}
}
